package com.duowan.common.web.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 */
public class InvalidJavascriptVarException extends CodeException {

    public InvalidJavascriptVarException() {
        super(-400, "Javascript 变量名称格式非法");
    }

    public InvalidJavascriptVarException(int code) {
        super(code);
    }

    public InvalidJavascriptVarException(int code, String message) {
        super(code, message);
    }
}
