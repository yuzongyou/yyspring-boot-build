package com.duowan.common.utils.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 * @since 2018/4/10 21:06
 */
public class AssertFailException extends CodeException {

    public AssertFailException(String message) {
        super(400, message);
    }

    public AssertFailException(int code, String message) {
        super(code, message);
    }
}
