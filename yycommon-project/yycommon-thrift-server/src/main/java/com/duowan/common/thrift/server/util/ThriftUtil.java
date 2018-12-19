package com.duowan.common.thrift.server.util;

import com.duowan.common.thrift.server.exception.ThriftServerException;
import org.apache.thrift.TProcessor;

import java.lang.reflect.Constructor;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 9:06
 */
public class ThriftUtil {

    private ThriftUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static TProcessor createThriftServiceProcessor(Class<?> thriftServiceClass, Class<?> ifaceClass, Object service) {

        Class<? extends TProcessor> processorClass = getProcessorClass(thriftServiceClass);

        try {
            Constructor<?> constructor = processorClass.getConstructor(ifaceClass);

            return (TProcessor) constructor.newInstance(service);
        } catch (Exception e) {
            throw new ThriftServerException(e);
        }

    }

    public static Class<? extends TProcessor> getProcessorClass(Class<?> thriftServiceClass) {
        return getServiceInnerClass(thriftServiceClass, TProcessor.class, "$Processor");
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
