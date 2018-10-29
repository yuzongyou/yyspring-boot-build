package com.duowan.yyspringcloud.msauth.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/17 20:01
 */
public class AuthenticationServerErrorException extends CodeException {

    public AuthenticationServerErrorException() {
    }

    public AuthenticationServerErrorException(int code) {
        super(code);
    }

    public AuthenticationServerErrorException(String message) {
        super(message);
    }

    public AuthenticationServerErrorException(int code, String message) {
        super(code, message);
    }

    public AuthenticationServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationServerErrorException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AuthenticationServerErrorException(Throwable cause) {
        super(cause);
    }

    public AuthenticationServerErrorException(int code, Throwable cause) {
        super(code, cause);
    }

    public AuthenticationServerErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AuthenticationServerErrorException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
