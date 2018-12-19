package com.duowan.common.utils.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 */
public class UtilsException extends CodeException {

    public UtilsException() {
    }

    public UtilsException(int code) {
        super(code);
    }

    public UtilsException(String message) {
        super(message);
    }

    public UtilsException(int code, String message) {
        super(code, message);
    }

    public UtilsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UtilsException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public UtilsException(Throwable cause) {
        super(cause);
    }

    public UtilsException(int code, Throwable cause) {
        super(code, cause);
    }

    public UtilsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UtilsException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
