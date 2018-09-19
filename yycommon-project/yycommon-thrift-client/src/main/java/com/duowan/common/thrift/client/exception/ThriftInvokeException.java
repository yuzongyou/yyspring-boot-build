package com.duowan.common.thrift.client.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/18 16:16
 */
public class ThriftInvokeException extends RuntimeException {

    public ThriftInvokeException() {
    }

    public ThriftInvokeException(String message) {
        super(message);
    }

    public ThriftInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThriftInvokeException(Throwable cause) {
        super(cause);
    }

    public ThriftInvokeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
