package com.duowan.common.admincenter.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 12:59
 */
public class NoPrivilegeException extends CodeException {

    private static final int ERROR_CODE = 403;

    public NoPrivilegeException() {
        super(ERROR_CODE, "没有权限！");
    }

    public NoPrivilegeException(String message) {
        super(ERROR_CODE, message);
    }

    public NoPrivilegeException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }

    public NoPrivilegeException(Throwable cause) {
        super(ERROR_CODE, null, cause);
    }

    public NoPrivilegeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(ERROR_CODE, message, cause, enableSuppression, writableStackTrace);
    }

}
