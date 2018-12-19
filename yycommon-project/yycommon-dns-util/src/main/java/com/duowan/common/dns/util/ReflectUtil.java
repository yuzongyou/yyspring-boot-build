package com.duowan.common.dns.util;

import java.lang.reflect.Field;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/7 9:13
 */
public class ReflectUtil {

    private ReflectUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取属性实例
     *
     * @param sourceClass  来源类
     * @param requiredType 类型
     * @param fieldName    属性名称
     * @param <T>          结果类型
     * @return 返回属性结果
     * @throws Exception 计算错误抛出任何可能的异常
     */
    public static <T> T getFieldInstance(Class<?> sourceClass, Class<T> requiredType, String fieldName) throws Exception {

        Field field = null;

        try {
            field = sourceClass.getDeclaredField(fieldName);

            field.setAccessible(true);

            Object obj = field.get(sourceClass);

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
     * 设置属性实例
     *
     * @param fieldName 属性名称
     * @param instance  实例
     * @throws Exception 任何异常
     */
    public static void setFieldInstance(Class<?> sourceClass, String fieldName, Object instance) throws Exception {

        Field field = null;

        try {
            field = sourceClass.getDeclaredField(fieldName);

            field.setAccessible(true);

            field.set(sourceClass, instance);
        } finally {
            if (null != field) {
                try {
                    field.setAccessible(false);
                } catch (Exception ignored) {
                }
            }
        }
    }
}
