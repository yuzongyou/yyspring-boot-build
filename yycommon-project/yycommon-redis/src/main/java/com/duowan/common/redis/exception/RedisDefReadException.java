package com.duowan.common.redis.exception;

/**
 * @author Arvin
 */
public class RedisDefReadException extends RedisException {

    public RedisDefReadException() {
    }

    public RedisDefReadException(int code) {
        super(code);
    }

    public RedisDefReadException(String message) {
        super(message);
    }

    public RedisDefReadException(int code, String message) {
        super(code, message);
    }

    public RedisDefReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisDefReadException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public RedisDefReadException(Throwable cause) {
        super(cause);
    }

    public RedisDefReadException(int code, Throwable cause) {
        super(code, cause);
    }

    public RedisDefReadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RedisDefReadException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
