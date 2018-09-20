package com.duowan.common.thrift.client.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 9:47
 */
public class ServerNodeDiscoveryNotFoundException extends RuntimeException {

    public ServerNodeDiscoveryNotFoundException() {
    }

    public ServerNodeDiscoveryNotFoundException(String message) {
        super(message);
    }

    public ServerNodeDiscoveryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerNodeDiscoveryNotFoundException(Throwable cause) {
        super(cause);
    }

    public ServerNodeDiscoveryNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
