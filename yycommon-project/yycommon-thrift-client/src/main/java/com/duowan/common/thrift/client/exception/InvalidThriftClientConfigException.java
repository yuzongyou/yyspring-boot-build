package com.duowan.common.thrift.client.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/18 17:05
 */
public class InvalidThriftClientConfigException extends RuntimeException {

    public InvalidThriftClientConfigException() {
    }

    public InvalidThriftClientConfigException(String message) {
        super(message);
    }

    public InvalidThriftClientConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidThriftClientConfigException(Throwable cause) {
        super(cause);
    }

    public InvalidThriftClientConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
