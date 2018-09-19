package com.duowan.common.thrift.client.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 9:47
 */
public class ServiceClassNotFoundException extends RuntimeException {

    public ServiceClassNotFoundException() {
    }

    public ServiceClassNotFoundException(String message) {
        super(message);
    }

    public ServiceClassNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceClassNotFoundException(Throwable cause) {
        super(cause);
    }

    public ServiceClassNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
