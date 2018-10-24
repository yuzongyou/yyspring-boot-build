package com.duowan.common.utils.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/24 15:22
 */
public class InvalidURISyntaxException extends CodeException {

    public InvalidURISyntaxException() {
        super(-400);
    }

    public InvalidURISyntaxException(int code) {
        super(code);
    }

    public InvalidURISyntaxException(String message) {
        super(-400, message);
    }

    public InvalidURISyntaxException(int code, String message) {
        super(code, message);
    }

    public InvalidURISyntaxException(String message, Throwable cause) {
        super(-400, message, cause);
    }

    public InvalidURISyntaxException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public InvalidURISyntaxException(Throwable cause) {
        super(-400, cause);
    }

    public InvalidURISyntaxException(int code, Throwable cause) {
        super(code, cause);
    }

    public InvalidURISyntaxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(-400, message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidURISyntaxException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
