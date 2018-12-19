package com.duowan.common.admincenter.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/19 12:00
 */
public class PrivilegeParseException extends CodeException {

    private static final int ERROR_CODE = 400;

    public PrivilegeParseException() {
        super(ERROR_CODE, "privilege.xml 解析异常！");
    }

    public PrivilegeParseException(String message) {
        super(ERROR_CODE, message);
    }

    public PrivilegeParseException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }

    public PrivilegeParseException(Throwable cause) {
        super(ERROR_CODE, null, cause);
    }

    public PrivilegeParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(ERROR_CODE, message, cause, enableSuppression, writableStackTrace);
    }
}
