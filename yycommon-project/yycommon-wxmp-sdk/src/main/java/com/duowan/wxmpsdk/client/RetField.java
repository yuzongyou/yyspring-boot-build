package com.duowan.wxmpsdk.client;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/29 17:17
 */
public class RetField {

    private RetField() {
        throw new IllegalStateException("Constants class");
    }

    public static final String SESSION_KEY = "session_key";
    public static final String OPENID = "openid";
    public static final String UNIONID = "unionid";
    public static final String ERROR_CODE = "errcode";
    public static final String ERROR_MSG = "errMsg";
}
