package com.duowan.common.thrift.client.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 20:59
 */
public class ThriftClientConfigException extends RuntimeException {

    public ThriftClientConfigException(String message) {
        super(message);
    }

    public ThriftClientConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}