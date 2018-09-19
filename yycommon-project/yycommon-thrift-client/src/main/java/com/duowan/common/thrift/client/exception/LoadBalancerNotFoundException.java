package com.duowan.common.thrift.client.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 9:47
 */
public class LoadBalancerNotFoundException extends RuntimeException {

    public LoadBalancerNotFoundException() {
    }

    public LoadBalancerNotFoundException(String message) {
        super(message);
    }

    public LoadBalancerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadBalancerNotFoundException(Throwable cause) {
        super(cause);
    }

    public LoadBalancerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
