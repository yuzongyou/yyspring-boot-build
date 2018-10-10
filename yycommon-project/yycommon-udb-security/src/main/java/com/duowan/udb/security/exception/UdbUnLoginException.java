package com.duowan.udb.security.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/10 10:44
 */
public class UdbUnLoginException extends CodeException {

    public UdbUnLoginException() {
        super(-403, "未登录UDB授权帐号！");
    }

    public UdbUnLoginException(int code) {
        super(code);
    }

    public UdbUnLoginException(String message) {
        super(message);
    }

    public UdbUnLoginException(int code, String message) {
        super(code, message);
    }

    public UdbUnLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public UdbUnLoginException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public UdbUnLoginException(Throwable cause) {
        super(cause);
    }

    public UdbUnLoginException(int code, Throwable cause) {
        super(code, cause);
    }

    public UdbUnLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UdbUnLoginException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
