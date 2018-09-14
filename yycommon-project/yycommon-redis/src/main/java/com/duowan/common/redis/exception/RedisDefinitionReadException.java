package com.duowan.common.redis.exception;

/**
 * @author Arvin
 */
public class RedisDefinitionReadException extends RedisException {

    public RedisDefinitionReadException() {
    }

    public RedisDefinitionReadException(int code) {
        super(code);
    }

    public RedisDefinitionReadException(String message) {
        super(message);
    }

    public RedisDefinitionReadException(int code, String message) {
        super(code, message);
    }

    public RedisDefinitionReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisDefinitionReadException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public RedisDefinitionReadException(Throwable cause) {
        super(cause);
    }

    public RedisDefinitionReadException(int code, Throwable cause) {
        super(code, cause);
    }

    public RedisDefinitionReadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RedisDefinitionReadException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
