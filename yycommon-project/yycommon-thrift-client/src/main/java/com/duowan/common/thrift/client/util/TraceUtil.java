package com.duowan.common.thrift.client.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/18 18:04
 */
public class TraceUtil {

    private TraceUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String generateTraceId() {
        return RandomStringUtils.random(10, true, true);
    }
}
