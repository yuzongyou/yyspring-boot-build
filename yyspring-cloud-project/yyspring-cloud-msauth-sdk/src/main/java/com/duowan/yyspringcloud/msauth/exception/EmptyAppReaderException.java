package com.duowan.yyspringcloud.msauth.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 9:35
 */
public class EmptyAppReaderException extends CodeException {

    public EmptyAppReaderException() {
    }

    public EmptyAppReaderException(int code) {
        super(code);
    }

    public EmptyAppReaderException(String message) {
        super(message);
    }

    public EmptyAppReaderException(int code, String message) {
        super(code, message);
    }

    public EmptyAppReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyAppReaderException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public EmptyAppReaderException(Throwable cause) {
        super(cause);
    }

    public EmptyAppReaderException(int code, Throwable cause) {
        super(code, cause);
    }

    public EmptyAppReaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EmptyAppReaderException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
