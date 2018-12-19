package com.duowan.common.codec.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/19 13:24
 */
public class CodecException extends CodeException {

    private static final int ERROR_CODE = 400;

    public CodecException() {
        super(ERROR_CODE, "编解码错误！");
    }

    public CodecException(String message) {
        super(ERROR_CODE, message);
    }

    public CodecException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }

    public CodecException(Throwable cause) {
        super(ERROR_CODE, null, cause);
    }

    public CodecException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(ERROR_CODE, message, cause, enableSuppression, writableStackTrace);
    }
}
