package com.duowan.common.exception;

/**
 * @author Arvin
 */
public class CodeException extends RuntimeException {

    /**
     * 异常代码
     */
    private final int code;

    public CodeException() {
        this(500);
    }

    public CodeException(int code) {
        super();
        this.code = code;
    }

    public CodeException(String message) {
        this(500, message);
    }

    public CodeException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CodeException(String message, Throwable cause) {
        this(500, message, cause);
    }

    public CodeException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public CodeException(Throwable cause) {
        this(500, cause);
    }

    public CodeException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public CodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        this(500, message, cause, enableSuppression, writableStackTrace);
    }

    public CodeException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
