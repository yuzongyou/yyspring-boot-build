package com.duowan.common.dns.util;

import sun.net.spi.nameservice.NameService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.*;

/**
 * InetAddress 操作工具类
 *
 * @author Arvin
 */
public class InetAddressUtil {

    private static final Class<?> INET_ADDRESS_CLASS = InetAddress.class;

    private static final LinkedHashMap<String, Object> ORIGIN_POSITIVE_ADDRESS_CACHE = lookupOriginPositiveAddressCache();

    /**
     * JDK 1.7 InetAddress 对象默认的NameService对象列表
     */
    private static final List<NameService> ORIGIN_NAME_SERVICES = lookupOriginNameServices();

    /**
     * JDK 1.6 InetAddress 对象默认的NameService对象
     */
    private static final NameService ORIGIN_NAME_SERVICE = lookupOriginNameService();

    /**
     * JDK 1.6 是使用单个 NameService 进行获取DNS解析
     */
    private static NameService lookupOriginNameService() {

        try {

            return getFieldInstance(NameService.class, "nameService");

        } catch (Exception ignored) {
        }

        return null;
    }

    /**
     * JDK 1.7 是使用多个 NameService 进行获取DNS解析
     */
    @SuppressWarnings({"unchecked"})
    private static List<NameService> lookupOriginNameServices() {

        try {

            return getFieldInstance(List.class, "nameServices");

        } catch (Exception ignored) {
        }

        return null;
    }

    @SuppressWarnings({"unchecked"})
    private static LinkedHashMap<String, Object> lookupOriginPositiveAddressCache() {
        try {
            return getPositiveAddressCache();
        } catch (Exception e) {
            return new LinkedHashMap<String, Object>();
        }
    }

    /**
     * 获取属性示例
     *
     * @param requiredType 类型
     * @param fieldName    属性名称
     * @param <T>          结果类型
     * @return 返回属性结果
     */
    public static <T> T getFieldInstance(Class<T> requiredType, String fieldName) throws Exception {
        Class<?> clazz = INET_ADDRESS_CLASS;

        Field field = null;
        field = null;

        try {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            Object obj = field.get(clazz);

            return null == obj ? null : requiredType.cast(obj);

        } finally {
            if (null != field) {
                try {
                    field.setAccessible(false);
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * 获取属性示例
     *
     * @param fieldName 属性名称
     * @param instance  实例
     * @throws Exception 任何异常
     */
    public static void setFieldInstance(String fieldName, Object instance) throws Exception {
        Class<?> clazz = INET_ADDRESS_CLASS;

        Field field = null;
        field = null;

        try {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            field.set(INET_ADDRESS_CLASS, instance);
        } finally {
            if (null != field) {
                try {
                    field.setAccessible(false);
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * 是否已经 wrap 过了
     *
     * @return 比较原始的是否一致
     */
    public static boolean hadCustomNameServiceWrap() {

        if (null != ORIGIN_NAME_SERVICE) {
            return !ORIGIN_NAME_SERVICE.equals(lookupOriginNameService());
        }

        if (null != ORIGIN_NAME_SERVICES) {
            return !ORIGIN_NAME_SERVICES.equals(lookupOriginNameServices());
        }
        return false;
    }


    /**
     * 添加为第一个生效的 NameService
     *
     * @param nameService 要添加的 NameSerivce
     * @param interceptor 拦截器
     */
    public synchronized static void addAsFirst(NameService nameService, NameServiceInterceptor interceptor) {
        if (null == nameService) {
            throw new RuntimeException("要设置的 NameService 不能为NULL！");
        }

        if (hadCustomNameServiceWrap()) {
            return;
        }

        try {
            List<NameService> tempList = new ArrayList<NameService>();
            tempList.add(nameService);

            boolean isJdk6 = ORIGIN_NAME_SERVICE != null;

            if (isJdk6) {
                // JDK6
                tempList.add(ORIGIN_NAME_SERVICE);
            } else {
                tempList.addAll(ORIGIN_NAME_SERVICES);
            }

            NameService wrapper = new NameServiceListWrapper(interceptor, tempList);

            if (isJdk6) {
                setFieldInstance("nameService", wrapper);
            } else {
                List<NameService> list = Collections.singletonList(wrapper);
                setFieldInstance("nameServices", list);
            }
        } catch (Exception e) {
            throw new RuntimeException("设置优先级别为1的NameService 失败：" + e.getMessage(), e);
        }
    }

    /**
     * 仅仅只是包装一层
     *
     * @param interceptor 拦截器
     */
    public synchronized static void wrapNameService(NameServiceInterceptor interceptor) {
        if (hadCustomNameServiceWrap()) {
            return;
        }

        try {
            List<NameService> tempList = new ArrayList<NameService>();

            boolean isJdk6 = ORIGIN_NAME_SERVICE != null;

            if (isJdk6) {
                // JDK6
                tempList.add(ORIGIN_NAME_SERVICE);
            } else {
                tempList.addAll(ORIGIN_NAME_SERVICES);
            }

            NameService wrapper = new NameServiceListWrapper(interceptor, tempList);

            if (isJdk6) {
                setFieldInstance("nameService", wrapper);
            } else {
                List<NameService> list = Collections.singletonList(wrapper);
                setFieldInstance("nameServices", list);
            }

        } catch (Exception e) {
            throw new RuntimeException("Wrap NameService 失败：" + e.getMessage(), e);
        }
    }

    /**
     * 取消自定义的 NameService
     */
    public static synchronized void unwrapNameService() {

        boolean isJdk6 = ORIGIN_NAME_SERVICE != null;

        try {
            if (isJdk6) {
                setFieldInstance("nameService", ORIGIN_NAME_SERVICE);
            } else {
                setFieldInstance("nameServices", ORIGIN_NAME_SERVICES);
            }
        } catch (Exception ignored) {
        }

    }

    /**
     * 获取 InetAddress DNS 缓存
     *
     * @param cacheType 缓存类型
     * @return 返回 缓存
     */
    @SuppressWarnings({"unchecked"})
    protected static LinkedHashMap<String, Object> getAddressCache(String cacheType) {
        try {
            final Field cacheField = InetAddress.class.getDeclaredField(cacheType);
            cacheField.setAccessible(true);
            final Object addressCache = cacheField.get(InetAddress.class);

            Class clazz = addressCache.getClass();
            final Field cacheMapField = clazz.getDeclaredField("cache");
            cacheMapField.setAccessible(true);
            return (LinkedHashMap) cacheMapField.get(addressCache);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获取 InetAddress DNS 缓存
     *
     * @param cacheType 缓存类型
     * @param map       缓存MAP
     */
    @SuppressWarnings({"unchecked"})
    protected static void setAddressCacheMap(String cacheType, Map<String, Object> map) {
        try {
            final Field cacheField = InetAddress.class.getDeclaredField(cacheType);
            cacheField.setAccessible(true);
            final Object addressCache = cacheField.get(InetAddress.class);

            Class clazz = addressCache.getClass();
            final Field cacheMapField = clazz.getDeclaredField("cache");
            cacheMapField.setAccessible(true);
            cacheMapField.set(addressCache, map);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 是否已经 wrap 过了
     *
     * @return 比较原始的是否一致
     */
    public static boolean hadCustomPositiveAddressCache() {
        LinkedHashMap<String, Object> origin = getPositiveAddressCache();
        if (null != origin && origin.equals(ORIGIN_POSITIVE_ADDRESS_CACHE)) {
            return false;
        }
        return true;
    }

    /**
     * 包装缓存 map
     *
     * @param interceptor 拦截器
     */
    public static synchronized void wrapPositiveAddressCache(DnsCacheInterceptor interceptor) {

        try {
            if (interceptor == null) {
                return;
            }

            if (hadCustomPositiveAddressCache()) {
                return;
            }

            LinkedHashMap<String, Object> cacheWrapper = new DnsCacheWrapper(interceptor);

            setAddressCacheMap("addressCache", cacheWrapper);
        } catch (Exception e) {
            throw new RuntimeException("Wrap Positive Address Cache Map failed: " + e.getMessage(), e);
        }

    }

    /**
     * 去掉包装，使用默认的
     */
    public static synchronized void unwrapPositiveAddressCache() {
        if (hadCustomPositiveAddressCache()) {
            setAddressCacheMap("addressCache", ORIGIN_POSITIVE_ADDRESS_CACHE);
        }
    }

    public static LinkedHashMap<String, Object> getPositiveAddressCache() {
        return getAddressCache("addressCache");
    }

    public static LinkedHashMap<String, Object> getNegativeAddressCache() {
        return getAddressCache("negativeCache");
    }

    /**
     * 从缓存获取结果
     *
     * @param host host
     * @return 不存在或者过期返回 null
     */
    public static InetAddress[] getAddressFromCache(String host) {

        try {

            String methodName;
            if (ORIGIN_NAME_SERVICE == null) {
                methodName = "getCachedAddresses";
            } else {
                methodName = "getCachedAddress";
            }

            final Class<InetAddress> clazz = InetAddress.class;
            final Method method = clazz.getDeclaredMethod(methodName, String.class);


            method.setAccessible(true);

            return (InetAddress[]) method.invoke(clazz, host);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    /**
     * 从NameService获取域名解析结果（同时也会缓存）
     *
     * @param host 主机地址
     * @return 返回NameService 解析的结果
     */
    public static InetAddress[] getAddressesFromNameService(String host) {
        try {

            String methodName;
            if (ORIGIN_NAME_SERVICE != null) {
                methodName = "getAddressFromNameService";
            } else {
                methodName = "getAddressesFromNameService";
            }

            final Class<InetAddress> clazz = InetAddress.class;
            final Method method = clazz.getDeclaredMethod(methodName, String.class, InetAddress.class);

            method.setAccessible(true);

            return (InetAddress[]) method.invoke(clazz, host, null);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
