package com.duowan.common.thrift.client.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 9:47
 */
public class TTransportFactoryNotFoundException extends RuntimeException {

    public TTransportFactoryNotFoundException() {
    }

    public TTransportFactoryNotFoundException(String message) {
        super(message);
    }

    public TTransportFactoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TTransportFactoryNotFoundException(Throwable cause) {
        super(cause);
    }

    public TTransportFactoryNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
