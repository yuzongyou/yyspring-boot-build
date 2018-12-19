package com.duowan.common.thrift.server.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 14:12
 */
public class ThriftServerException extends CodeException {

    public ThriftServerException() {
    }

    public ThriftServerException(int code) {
        super(code);
    }

    public ThriftServerException(String message) {
        super(message);
    }

    public ThriftServerException(int code, String message) {
        super(code, message);
    }

    public ThriftServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThriftServerException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ThriftServerException(Throwable cause) {
        super(cause);
    }

    public ThriftServerException(int code, Throwable cause) {
        super(code, cause);
    }

    public ThriftServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ThriftServerException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
