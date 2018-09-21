package com.duowan.common.thrift.server.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 9:18
 */
public class DuplicateIfaceFoundException extends RuntimeException {

    public DuplicateIfaceFoundException() {
    }

    public DuplicateIfaceFoundException(String message) {
        super(message);
    }

    public DuplicateIfaceFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateIfaceFoundException(Throwable cause) {
        super(cause);
    }

    public DuplicateIfaceFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
