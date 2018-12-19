package com.duowan.wxmpsdk.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/30 8:54
 */
public class ExceptionCode {

    private ExceptionCode() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * Code 换取 Session 失败
     **/
    public static final int ERROR_CODE_TO_SESSION = 20001;
}
