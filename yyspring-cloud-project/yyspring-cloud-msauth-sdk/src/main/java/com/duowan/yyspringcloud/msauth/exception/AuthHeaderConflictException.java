package com.duowan.yyspringcloud.msauth.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 11:09
 */
public class AuthHeaderConflictException extends CodeException {

    public AuthHeaderConflictException() {
    }

    public AuthHeaderConflictException(int code) {
        super(code);
    }

    public AuthHeaderConflictException(String message) {
        super(message);
    }

    public AuthHeaderConflictException(int code, String message) {
        super(code, message);
    }

    public AuthHeaderConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthHeaderConflictException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AuthHeaderConflictException(Throwable cause) {
        super(cause);
    }

    public AuthHeaderConflictException(int code, Throwable cause) {
        super(code, cause);
    }

    public AuthHeaderConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AuthHeaderConflictException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
