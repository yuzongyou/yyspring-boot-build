package com.duowan.common.ipowner.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/19 13:24
 */
public class IpownerException extends CodeException {

    private static final int ERROR_CODE = 400;

    public IpownerException() {
        super(ERROR_CODE, "Ip归属错误！");
    }

    public IpownerException(String message) {
        super(ERROR_CODE, message);
    }

    public IpownerException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }

    public IpownerException(Throwable cause) {
        super(ERROR_CODE, null, cause);
    }

    public IpownerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(ERROR_CODE, message, cause, enableSuppression, writableStackTrace);
    }
}
