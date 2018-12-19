package com.duowan.common.thrift.client.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 20:59
 */
public class ThriftClientException extends CodeException {

    public ThriftClientException() {
    }

    public ThriftClientException(int code) {
        super(code);
    }

    public ThriftClientException(String message) {
        super(message);
    }

    public ThriftClientException(int code, String message) {
        super(code, message);
    }

    public ThriftClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThriftClientException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ThriftClientException(Throwable cause) {
        super(cause);
    }

    public ThriftClientException(int code, Throwable cause) {
        super(code, cause);
    }

    public ThriftClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ThriftClientException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}