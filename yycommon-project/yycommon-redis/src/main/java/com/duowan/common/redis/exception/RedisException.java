package com.duowan.common.redis.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 */
public class RedisException extends CodeException {

    public RedisException() {
    }

    public RedisException(int code) {
        super(code);
    }

    public RedisException(String message) {
        super(message);
    }

    public RedisException(int code, String message) {
        super(code, message);
    }

    public RedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public RedisException(Throwable cause) {
        super(cause);
    }

    public RedisException(int code, Throwable cause) {
        super(code, cause);
    }

    public RedisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RedisException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
