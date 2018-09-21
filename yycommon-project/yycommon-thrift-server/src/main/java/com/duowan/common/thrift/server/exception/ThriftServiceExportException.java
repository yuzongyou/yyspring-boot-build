package com.duowan.common.thrift.server.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 14:12
 */
public class ThriftServiceExportException extends RuntimeException {

    public ThriftServiceExportException() {
    }

    public ThriftServiceExportException(String message) {
        super(message);
    }

    public ThriftServiceExportException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThriftServiceExportException(Throwable cause) {
        super(cause);
    }

    public ThriftServiceExportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
