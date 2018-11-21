package com.duowan.common.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/20 20:56
 */
public class HttpResponseConvertException extends CodeException {

    public HttpResponseConvertException() {
    }

    public HttpResponseConvertException(int code) {
        super(code);
    }

    public HttpResponseConvertException(String message) {
        super(message);
    }

    public HttpResponseConvertException(int code, String message) {
        super(code, message);
    }

    public HttpResponseConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpResponseConvertException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public HttpResponseConvertException(Throwable cause) {
        super(cause);
    }

    public HttpResponseConvertException(int code, Throwable cause) {
        super(code, cause);
    }

    public HttpResponseConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HttpResponseConvertException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
