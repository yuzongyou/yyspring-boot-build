package com.duowan.common.thrift.client.util;

import com.duowan.common.thrift.client.ClientType;
import com.duowan.common.utils.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.async.TAsyncClient;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 20:11
 */
public class ThriftUtil {

    private ThriftUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Class<? extends TServiceClient> getTServiceClientClass(String serviceClass) {
        return getServiceInnerClass(getServiceClass(serviceClass), TServiceClient.class, "$Client");
    }

    public static Class<? extends TServiceClient> getTServiceClientClass(Class<?> serviceClass) {
        return getServiceInnerClass(serviceClass, TServiceClient.class, "$Client");
    }

    public static Class<? extends TAsyncClient> getTAsyncClientClass(String serviceClass) {
        return getServiceInnerClass(getServiceClass(serviceClass), TAsyncClient.class, "$AsyncClient");
    }

    public static Class<? extends TAsyncClient> getTAsyncClientClass(Class<?> serviceClass) {
        return getServiceInnerClass(serviceClass, TAsyncClient.class, "$AsyncClient");
    }

    public static Class<?> getIfaceClass(String serviceClass) {
        return getServiceInnerClass(getServiceClass(serviceClass), null, "$Iface");
    }

    public static Class<?> getIfaceClass(Class<?> serviceClass) {
        return getServiceInnerClass(serviceClass, null, "$Iface");
    }

    private static Class<?> getServiceClass(String serviceClass) {
        try {
            return Class.forName(serviceClass);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Thrift Service Class[" + serviceClass + "] 不存在！");
        }
    }

    @SuppressWarnings({"unchecked"})
    private static <T> Class<T> getServiceInnerClass(Class<?> serviceClass, Class<T> targetClass, String innerClassSuffix) {
        Class<?>[] classes = serviceClass.getDeclaredClasses();

        for (Class<?> clazz : classes) {
            boolean isTargetClass = null == targetClass || targetClass.isAssignableFrom(clazz);
            if (isTargetClass && clazz.getName().endsWith(innerClassSuffix)) {
                return (Class<T>) clazz;
            }
        }

        throw new IllegalArgumentException("[" + serviceClass + "] 无法找到[" + targetClass + "] 类");
    }

    public static String fixRouter(String router) {
        if (StringUtils.isBlank(router)) {
            return null;
        }
        return router.trim().toUpperCase();
    }

    public static String toNotNullRouter(String router) {
        router = fixRouter(router);
        return router == null ? "" : router;
    }

    public static String argsToString(Object[] args) {
        StringBuilder builder = new StringBuilder("[");

        if (args == null || args.length < 1) {
            builder.append("null]");
        } else {
            for (Object arg : args) {
                builder.append(arg).append(",");
            }
            builder.setLength(builder.length() - 1);
            builder.append("]");
        }

        return builder.toString();
    }

    public static String buildDefaultThriftClientBeanName(Class<?> serviceClass, String router, ClientType clientType) {

        String clientTypeName = "Iface";
        if (ClientType.ASYNC_IFACE.equals(clientType)) {
            clientTypeName = "AsyncIface";
        }

        if (StringUtils.isBlank(router)) {
            return CommonUtil.firstLetterToLowerCase(serviceClass.getSimpleName()) + clientTypeName;
        }
        return CommonUtil.firstLetterToLowerCase(serviceClass.getSimpleName())
                + CommonUtil.firstLetterToUpperCase(router.trim())
                + clientTypeName;
    }
}
