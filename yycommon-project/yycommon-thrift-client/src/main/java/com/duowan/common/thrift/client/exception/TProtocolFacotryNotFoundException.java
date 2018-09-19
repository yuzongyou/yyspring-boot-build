package com.duowan.common.thrift.client.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 9:47
 */
public class TProtocolFacotryNotFoundException extends RuntimeException {

    public TProtocolFacotryNotFoundException() {
    }

    public TProtocolFacotryNotFoundException(String message) {
        super(message);
    }

    public TProtocolFacotryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TProtocolFacotryNotFoundException(Throwable cause) {
        super(cause);
    }

    public TProtocolFacotryNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
