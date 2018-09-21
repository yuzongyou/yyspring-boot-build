package com.duowan.common.thrift.server.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 10:13
 */
public class CanNotDeduceTServerException extends RuntimeException {

    public CanNotDeduceTServerException() {
    }

    public CanNotDeduceTServerException(String message) {
        super(message);
    }

    public CanNotDeduceTServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotDeduceTServerException(Throwable cause) {
        super(cause);
    }

    public CanNotDeduceTServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
