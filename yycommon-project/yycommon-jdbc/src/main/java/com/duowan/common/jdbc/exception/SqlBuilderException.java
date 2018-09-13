package com.duowan.common.jdbc.exception;

/**
 * @author Arvin
 */
public class SqlBuilderException extends JdbcException {

    public SqlBuilderException() {
    }

    public SqlBuilderException(int code) {
        super(code);
    }

    public SqlBuilderException(String message) {
        super(message);
    }

    public SqlBuilderException(int code, String message) {
        super(code, message);
    }

    public SqlBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlBuilderException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public SqlBuilderException(Throwable cause) {
        super(cause);
    }

    public SqlBuilderException(int code, Throwable cause) {
        super(code, cause);
    }

    public SqlBuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SqlBuilderException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
