package com.duowan.common.thrift.client.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 16:34
 */
public class ThriftClientInjectException extends RuntimeException {

    public ThriftClientInjectException() {
    }

    public ThriftClientInjectException(String message) {
        super(message);
    }

    public ThriftClientInjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThriftClientInjectException(Throwable cause) {
        super(cause);
    }

    public ThriftClientInjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
