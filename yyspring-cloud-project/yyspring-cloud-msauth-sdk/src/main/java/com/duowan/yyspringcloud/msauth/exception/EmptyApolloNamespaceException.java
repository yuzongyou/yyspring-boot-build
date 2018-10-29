package com.duowan.yyspringcloud.msauth.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 9:42
 */
public class EmptyApolloNamespaceException extends CodeException {

    public EmptyApolloNamespaceException() {
    }

    public EmptyApolloNamespaceException(int code) {
        super(code);
    }

    public EmptyApolloNamespaceException(String message) {
        super(message);
    }

    public EmptyApolloNamespaceException(int code, String message) {
        super(code, message);
    }

    public EmptyApolloNamespaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyApolloNamespaceException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public EmptyApolloNamespaceException(Throwable cause) {
        super(cause);
    }

    public EmptyApolloNamespaceException(int code, Throwable cause) {
        super(code, cause);
    }

    public EmptyApolloNamespaceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EmptyApolloNamespaceException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
