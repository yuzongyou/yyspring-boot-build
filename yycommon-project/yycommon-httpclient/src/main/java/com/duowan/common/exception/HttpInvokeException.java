package com.duowan.common.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/20 20:56
 */
public class HttpInvokeException extends CodeException {

    private static final int ERROR_CODE = 400000;

    public HttpInvokeException() {
        super(ERROR_CODE);
    }

    public HttpInvokeException(Throwable cause) {
        super(ERROR_CODE, cause);
    }

    public HttpInvokeException(String message) {
        super(ERROR_CODE, message);
    }

    public HttpInvokeException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }


}
