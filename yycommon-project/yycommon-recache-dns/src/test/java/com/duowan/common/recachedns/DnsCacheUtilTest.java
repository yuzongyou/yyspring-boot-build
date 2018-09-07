package com.duowan.common.recachedns;

import com.duowan.common.dns.util.DnsInterceptor;
import com.duowan.common.dns.util.InetAddressUtil;
import com.duowan.common.virtualdns.VirtualDnsUtil;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/7 17:11
 */
public class DnsCacheUtilTest {

    private String addressesToString(InetAddress[] addresses) {
        StringBuilder builder = new StringBuilder("[");

        if (addresses.length > 0) {
            for (InetAddress inetAddress : addresses) {
                builder.append(inetAddress.getHostAddress()).append(",");
            }
            builder.setLength(builder.length() - 1);
        }
        builder.append("]");


        return builder.toString();
    }

    /**
     * DNS 缓存过期， addressCache 中不存在
     */
    @Test
    public void testCacheExpire() throws Exception {

        String host = "www.hello.com";

        DnsCacheUtil.setDnsInterceptor(new DnsInterceptor() {

            ThreadLocal<Long> startNanoTime = new ThreadLocal<Long>();

            @Override
            public void before(String host) {
                startNanoTime.set(System.nanoTime());
            }

            @Override
            public void after(String host, InetAddress[] addresses, Exception exception) {
                long currentNanoTime = System.nanoTime();
                long takeTime = currentNanoTime - startNanoTime.get();
                System.out.println("Host: [" + host + "] = {"+ addressesToString(addresses) + "} 解析耗时： " + takeTime + " 纳秒， 合计 " + (takeTime / 1000000f) + " 毫秒！");
            }
        });

        DnsCacheUtil.setDnsCacheTime(2);

        VirtualDnsUtil.enabled();
        VirtualDnsUtil.add(host, "127.0.0.1");


        Map<String, Object> cacheMap = InetAddressUtil.getPositiveAddressCache();

        assertTrue(!cacheMap.containsKey(host));

        // trigger dns access
        triggerHostAccess(host);
        assertTrue(cacheMap.containsKey(host));

        // 使得缓存过期
        Thread.sleep(2100);

        assertNull(InetAddressUtil.getAddressFromCache(host));

        DnsCacheUtil.enabledAutoReCache();
        // trigger dns access
        triggerHostAccess(host);
        assertNotNull(InetAddressUtil.getAddressFromCache(host));

        // 使得缓存过期
        Thread.sleep(2100);

        VirtualDnsUtil.add(host, "127.0.0.2");
        Thread.sleep(2100);

        InetAddress[] addresses = InetAddressUtil.getAddressFromCache(host);
        assertNotNull(addresses);
        System.out.println(addresses[0].getHostAddress());

        // 取消
        DnsCacheUtil.disabledAutoReCache();
        // 使得缓存过期
        Thread.sleep(2100);
        assertNull(InetAddressUtil.getAddressFromCache(host));

        DnsCacheUtil.enabledAutoReCache();

        Thread.sleep(2000);
        Thread.sleep(2100);

    }

    private void triggerHostAccess(String host) {
        try {
            InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}