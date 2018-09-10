package com.duowan.yyspringboot.autoconfigure.web.exception;

import com.duowan.common.exception.CodeException;

/**
 * 分页参数不正确
 *
 * @author Arvin
 */
public class InvalidPagingParamException extends CodeException {

    public InvalidPagingParamException() {
        super(-400, "分页参数不正确！");
    }

    public InvalidPagingParamException(int code) {
        super(code);
    }

    public InvalidPagingParamException(String message) {
        super(-400, message);
    }

    public InvalidPagingParamException(int code, String message) {
        super(code, message);
    }


}
