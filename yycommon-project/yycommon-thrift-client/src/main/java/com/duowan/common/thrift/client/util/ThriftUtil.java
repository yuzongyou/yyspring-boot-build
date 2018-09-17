package com.duowan.common.thrift.client.util;

import org.apache.thrift.TServiceClient;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 20:11
 */
public class ThriftUtil {

    public static Class<? extends TServiceClient> getTServiceClass(String serviceClass) {
        return getServiceInnerClass(getServiceClass(serviceClass), TServiceClient.class, "$Client");
    }

    public static Class<? extends TServiceClient> getTServiceClass(Class<?> serviceClass) {
        return getServiceInnerClass(serviceClass, TServiceClient.class, "$Client");
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

}
