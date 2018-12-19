package com.duowan.common.util;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/19 14:00
 */
public class HttpClientConstants {

    private HttpClientConstants() {
        throw new IllegalStateException("Utility Class");
    }

    public static final int STATUS_CODE_SUCCESS = 200;

    public static final String KEY_STATUS = "status";
    public static final String KEY_DATA = "data";
    public static final String KEY_MESSAGE = "message";
}
