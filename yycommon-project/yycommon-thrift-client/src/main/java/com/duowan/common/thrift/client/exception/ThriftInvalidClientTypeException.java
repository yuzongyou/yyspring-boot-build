package com.duowan.common.thrift.client.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/18 17:24
 */
public class ThriftInvalidClientTypeException extends RuntimeException {

    public ThriftInvalidClientTypeException() {
    }

    public ThriftInvalidClientTypeException(String message) {
        super(message);
    }

    public ThriftInvalidClientTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThriftInvalidClientTypeException(Throwable cause) {
        super(cause);
    }

    public ThriftInvalidClientTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
