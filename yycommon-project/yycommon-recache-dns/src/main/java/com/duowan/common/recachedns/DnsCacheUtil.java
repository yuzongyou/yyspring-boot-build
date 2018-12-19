package com.duowan.common.recachedns;

import com.duowan.common.dns.util.AbstractDnsCacheWrapper;
import com.duowan.common.dns.util.DnsInterceptor;
import com.duowan.common.dns.util.InetAddressUtil;
import sun.net.InetAddressCachePolicy;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.*;

/**
 * Dns 缓存工具类
 *
 * @author Arvin
 */
public class DnsCacheUtil {

    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(20);

    private static ScheduledExecutorService dnsRefreshExecutor;

    private static final Set<String> HAD_SCHEDULE_HOSTS = new HashSet<>();

    /**
     * DNS 解析拦截器
     */
    private static DnsInterceptor dnsInterceptor;

    /**
     * 调度结果 list
     */
    private static final List<ScheduledFuture<?>> SCHEDULE_FUTURE_LIST = new ArrayList<>();

    static {
        ThreadFactory factory = Executors.defaultThreadFactory();
        dnsRefreshExecutor = new ScheduledThreadPoolExecutor(20, factory);
    }


    /**
     * 刷新DNS
     *
     * @param host 主机地址
     */
    static void refreshDns(final String host) {
        if (null != host && !"".equals(host.trim())) {
            if (null != dnsInterceptor) {
                try {
                    dnsInterceptor.before(host);
                } catch (Exception ignored) {
                }
            }

            InetAddress[] addresses = null;
            Exception exception = null;

            try {
                addresses = InetAddressUtil.getAddressesFromNameService(host);
            } catch (Exception e) {
                exception = e;
                throw e;
            } finally {
                if (null != dnsInterceptor) {
                    try {
                        dnsInterceptor.after(host, addresses, exception);
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

    /**
     * 调度 DNS 自动刷新
     *
     * @param host               主机地址
     * @param runOnceImmediately 立即调用一次
     */
    static void scheduleDnsAutoRefreshCache(final String host, final boolean runOnceImmediately) {

        if (runOnceImmediately) {
            refreshDns(host);
        }

        if (HAD_SCHEDULE_HOSTS.contains(host)) {
            return;
        }

        synchronized (HAD_SCHEDULE_HOSTS) {
            if (!HAD_SCHEDULE_HOSTS.contains(host)) {
                HAD_SCHEDULE_HOSTS.add(host);

                int policy = InetAddressCachePolicy.get();
                boolean isNever = InetAddressCachePolicy.NEVER == policy;
                boolean isForever = InetAddressCachePolicy.FOREVER == policy;
                if (isForever || isNever) {
                    // 虽然是永久，但是不排除中途变了，加个调度
                    dnsRefreshExecutor.schedule(() -> {
                        synchronized (HAD_SCHEDULE_HOSTS) {
                            HAD_SCHEDULE_HOSTS.remove(host);
                        }
                    }, 20000, TimeUnit.MILLISECONDS);
                    return;
                }


                long expireMillis = policy * 1000L;
                long scheduleExpireMillis = expireMillis - getMaxDnsMillis();
                scheduleExpireMillis = scheduleExpireMillis < 1000 ? 1000 : scheduleExpireMillis;

                SCHEDULE_FUTURE_LIST.add(dnsRefreshExecutor.scheduleAtFixedRate(() -> {

                    try {
                        refreshDns(host);
                    } catch (Exception ignored) {
                    }
                }, scheduleExpireMillis, scheduleExpireMillis, TimeUnit.MILLISECONDS));
            }
        }
    }

    /**
     * 包装 Dns 缓存
     */
    private static void wrapDnsCache() {

        InetAddressUtil.wrapPositiveAddressCache(new AbstractDnsCacheWrapper() {
            @Override
            public void beforePut(final String host, final Object object) {
                try {
                    executor.execute(() -> {
                        try {
                            dnsStartTimeMap.put(host, System.currentTimeMillis());
                            scheduleDnsAutoRefreshCache(host, false);
                        } catch (Exception ignored) {
                        }
                    });
                } catch (Exception ignored) {
                }

            }
        });
    }

    private static void unwrapDnsCache() {
        InetAddressUtil.unwrapPositiveAddressCache();
    }

    /**
     * DNS 解析最大时间
     */
    private static volatile int maxDnsMillis = 15000;

    /**
     * JVM 历史解析过的 DNS域名列表
     */
    private static Map<String, Long> dnsStartTimeMap = new HashMap<>();

    /**
     * 默认DNS缓存时间
     */
    private static final int DEFAULT_JVM_DNS_CACHE_TIME = getDnsCacheTime();

    /**
     * 是否自动进行重新DNS缓存
     */
    private static volatile boolean autoReCache = false;

    /**
     * 是否自动进行重新缓存
     *
     * @return 返回是/否
     */
    public static boolean isAutoReCache() {
        return autoReCache;
    }

    /**
     * 启用自动重新缓存
     */
    public static void enabledAutoReCache() {
        autoReCache = true;

        reScheduleAllRefreshTask();

        wrapDnsCache();
    }

    static void reScheduleAllRefreshTask() {

        HAD_SCHEDULE_HOSTS.clear();

        // 重新调度host 进行 dns解析
        if (dnsStartTimeMap != null && !dnsStartTimeMap.isEmpty()) {
            Set<String> hostSet = dnsStartTimeMap.keySet();
            for (String host : hostSet) {
                scheduleDnsAutoRefreshCache(host, true);
            }
        }
    }

    public static DnsInterceptor getDnsInterceptor() {
        return dnsInterceptor;
    }

    public static void setDnsInterceptor(DnsInterceptor dnsInterceptor) {
        DnsCacheUtil.dnsInterceptor = dnsInterceptor;
    }

    /**
     * 禁用自动重新缓存
     */
    public static void disabledAutoReCache() {
        autoReCache = false;

        unwrapDnsCache();

        // 终止所有调度任务
        terminateAllScheduleTask();
    }

    static void terminateAllScheduleTask() {
        // 终止所有调度任务
        synchronized (SCHEDULE_FUTURE_LIST) {
            for (ScheduledFuture future : SCHEDULE_FUTURE_LIST) {
                future.cancel(false);
            }

            SCHEDULE_FUTURE_LIST.clear();
        }
    }

    public static int getMaxDnsMillis() {
        return maxDnsMillis;
    }

    /**
     * 设置 DNS 解析最大时间，预估值，传递一个最大的DNS解析时间
     *
     * @param maxDnsMillis DNS 最大解析时间，只是一个预估
     */
    public static void setMaxDnsMillis(int maxDnsMillis) {
        DnsCacheUtil.maxDnsMillis = maxDnsMillis < 1000 ? 15000 : maxDnsMillis;

        if (isAutoReCache()) {
            terminateAllScheduleTask();
            reScheduleAllRefreshTask();
        }
    }

    /**
     * 获取JVM 默认的DNS缓存时间
     *
     * @return 返回DNS缓存时间，单位是秒
     */
    public static int getDefaultJvmDnsCacheTime() {
        return DEFAULT_JVM_DNS_CACHE_TIME;
    }

    /**
     * 获取JVM DNS缓存时间，单位是秒
     *
     * @return DNS 缓存时间，单位是秒
     */
    public static int getDnsCacheTime() {
        return InetAddressCachePolicy.get();
    }

    /**
     * 设置 DNS 缓存时间
     *
     * @param seconds 缓存时间，单位是秒, 0 表示不缓存，-1表示永久缓存， 正数表示缓存的秒数
     */
    public static void setDnsCacheTime(int seconds) {
        int expire = seconds;
        if (seconds < 0) {
            expire = InetAddressCachePolicy.FOREVER;
        }

        Class<InetAddressCachePolicy> clazz = InetAddressCachePolicy.class;
        try {
            Field field = clazz.getDeclaredField("cachePolicy");
            field.setAccessible(true);
            field.set(clazz, expire);
            field.setAccessible(false);
        } catch (Exception ignored) {
        }

        try {
            Field setField = clazz.getDeclaredField("set");
            setField.setAccessible(true);
            setField.set(clazz, true);
            setField.setAccessible(false);
        } catch (Exception ignored) {
        }

        if (isAutoReCache()) {
            terminateAllScheduleTask();
            reScheduleAllRefreshTask();
        }
    }

    /**
     * 直接进行DNS获取，而不走DNS缓存
     *
     * @param host 主机地址
     * @return 返回直接进行DNS解析的结果（即不从JVM的DNS缓存中获取）
     */
    public static String getIpForDirectDns(String host) {

        InetAddress[] array = InetAddressUtil.getAddressesFromNameService(host);
        if (array != null && array.length > 0) {
            return array[0].getHostAddress();
        }

        return null;
    }

    public static String getIpWithCache(String host) {
        try {
            return InetAddress.getByName(host).getHostAddress();
        } catch (Exception e) {
            return null;
        }
    }

}
