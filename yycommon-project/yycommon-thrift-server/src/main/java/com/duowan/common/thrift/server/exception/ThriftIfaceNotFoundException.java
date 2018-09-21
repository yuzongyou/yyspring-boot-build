package com.duowan.common.thrift.server.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 9:14
 */
public class ThriftIfaceNotFoundException extends RuntimeException {

    public ThriftIfaceNotFoundException() {
    }

    public ThriftIfaceNotFoundException(String message) {
        super(message);
    }

    public ThriftIfaceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThriftIfaceNotFoundException(Throwable cause) {
        super(cause);
    }

    public ThriftIfaceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
