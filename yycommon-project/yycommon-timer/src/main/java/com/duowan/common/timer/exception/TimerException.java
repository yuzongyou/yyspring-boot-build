package com.duowan.common.timer.exception;

import com.duowan.common.exception.CodeException;

/**
 * @author Arvin
 */
public class TimerException extends CodeException {

    public TimerException() {
    }

    public TimerException(int code) {
        super(code);
    }

    public TimerException(String message) {
        super(message);
    }

    public TimerException(int code, String message) {
        super(code, message);
    }

    public TimerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimerException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public TimerException(Throwable cause) {
        super(cause);
    }

    public TimerException(int code, Throwable cause) {
        super(code, cause);
    }

    public TimerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TimerException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
