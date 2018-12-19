package com.duowan.common.web.exception.handler;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/19 10:37
 */
public class ErrorMessage {

    private final boolean canHandle;

    private final int errorCode;

    private final String message;

    private final Exception exception;

    public ErrorMessage(boolean canHandle, int errorCode, String message, Exception exception) {
        this.canHandle = canHandle;
        this.errorCode = errorCode;
        if (isMessageBlank(message) && exception != null) {
            message = exception.getMessage();
        }
        if (isMessageBlank(message)) {
            message = "服务器繁忙，请稍候再试";
        }
        this.message = message;
        this.exception = exception;
    }

    private boolean isMessageBlank(String message) {
        return null == message || "".equals(message.trim());
    }

    public boolean isCanHandle() {
        return canHandle;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public Exception getException() {
        return exception;
    }
}
