package com.duowan.common.jdbc.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 */
public class JdbcException extends CodeException {

    public JdbcException() {
    }

    public JdbcException(int code) {
        super(code);
    }

    public JdbcException(String message) {
        super(message);
    }

    public JdbcException(int code, String message) {
        super(code, message);
    }

    public JdbcException(String message, Throwable cause) {
        super(message, cause);
    }

    public JdbcException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public JdbcException(Throwable cause) {
        super(cause);
    }

    public JdbcException(int code, Throwable cause) {
        super(code, cause);
    }

    public JdbcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public JdbcException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
