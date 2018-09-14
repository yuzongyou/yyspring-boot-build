package com.duowan.common.web.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 * @since  2017/12/9 21:39
 */
public class InvalidJsonpCallbackException extends CodeException {

    public InvalidJsonpCallbackException() {
        super(-400, "JSONP 回调函数格式非法");
    }

    public InvalidJsonpCallbackException(int code) {
        super(code);
    }

    public InvalidJsonpCallbackException(int code, String message) {
        super(code, message);
    }
}
