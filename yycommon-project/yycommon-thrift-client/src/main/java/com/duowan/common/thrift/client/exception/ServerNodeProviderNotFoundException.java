package com.duowan.common.thrift.client.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 9:47
 */
public class ServerNodeProviderNotFoundException extends RuntimeException {

    public ServerNodeProviderNotFoundException() {
    }

    public ServerNodeProviderNotFoundException(String message) {
        super(message);
    }

    public ServerNodeProviderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerNodeProviderNotFoundException(Throwable cause) {
        super(cause);
    }

    public ServerNodeProviderNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
