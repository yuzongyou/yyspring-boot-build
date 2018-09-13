package com.duowan.common.jdbc.exception;

/**
 * @author Arvin
 */
public class ModelDefNotFoundException extends JdbcException {

    public ModelDefNotFoundException(Class<?> modelType) {
        super("模型[" + modelType.getName() + "]定义找不到");
    }

    public ModelDefNotFoundException(String message) {
        super(message);
    }

    public ModelDefNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelDefNotFoundException(Throwable cause) {
        super(cause);
    }

    public ModelDefNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
