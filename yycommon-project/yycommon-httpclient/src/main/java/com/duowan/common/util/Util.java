package com.duowan.common.util;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 18:01
 */
public class Util {

    public static boolean isBlank(String value) {
        return null == value || "".equals(value.trim());
    }

    public static boolean isNotBlank(String value) {
        return null != value && !"".equals(value.trim());
    }
}
