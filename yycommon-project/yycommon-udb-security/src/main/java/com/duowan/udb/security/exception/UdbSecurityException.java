package com.duowan.udb.security.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/10 10:44
 */
public class UdbSecurityException extends CodeException {

    public UdbSecurityException() {
        super(-403, "未登录UDB授权帐号！");
    }

    public UdbSecurityException(int code) {
        super(code);
    }

    public UdbSecurityException(String message) {
        super(message);
    }

    public UdbSecurityException(int code, String message) {
        super(code, message);
    }

    public UdbSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public UdbSecurityException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public UdbSecurityException(Throwable cause) {
        super(cause);
    }

    public UdbSecurityException(int code, Throwable cause) {
        super(code, cause);
    }

    public UdbSecurityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UdbSecurityException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
