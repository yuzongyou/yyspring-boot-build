package com.duowan.common.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/20 20:56
 */
public class InvalidHttpUrlException extends CodeException {

    private static final int ERROR_CODE = 400001;

    public InvalidHttpUrlException() {
        super(ERROR_CODE);
    }

    public InvalidHttpUrlException(Throwable cause) {
        super(ERROR_CODE, cause);
    }

    public InvalidHttpUrlException(String message) {
        super(ERROR_CODE, message);
    }

    public InvalidHttpUrlException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }


}
