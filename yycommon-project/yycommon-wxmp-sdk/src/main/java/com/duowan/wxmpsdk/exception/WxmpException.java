package com.duowan.wxmpsdk.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/30 8:53
 */
public class WxmpException extends CodeException {

    public WxmpException() {
    }

    public WxmpException(int code) {
        super(code);
    }

    public WxmpException(String message) {
        super(message);
    }

    public WxmpException(int code, String message) {
        super(code, message);
    }

    public WxmpException(String message, Throwable cause) {
        super(message, cause);
    }

    public WxmpException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public WxmpException(Throwable cause) {
        super(cause);
    }

    public WxmpException(int code, Throwable cause) {
        super(code, cause);
    }

    public WxmpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public WxmpException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
