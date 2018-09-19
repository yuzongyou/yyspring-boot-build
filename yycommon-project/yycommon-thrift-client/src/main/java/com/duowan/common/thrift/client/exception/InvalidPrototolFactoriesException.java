package com.duowan.common.thrift.client.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 9:25
 */
public class InvalidPrototolFactoriesException extends RuntimeException {

    public InvalidPrototolFactoriesException() {
    }

    public InvalidPrototolFactoriesException(String message) {
        super(message);
    }

    public InvalidPrototolFactoriesException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPrototolFactoriesException(Throwable cause) {
        super(cause);
    }

    public InvalidPrototolFactoriesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
