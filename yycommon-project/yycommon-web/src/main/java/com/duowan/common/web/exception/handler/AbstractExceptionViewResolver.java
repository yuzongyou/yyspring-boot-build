package com.duowan.common.web.exception.handler;

import com.duowan.common.exception.CodeException;
import com.duowan.common.web.i18n.I18nHelper;
import com.duowan.common.web.i18n.I18nKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @author Arvin
 */
public abstract class AbstractExceptionViewResolver implements ExceptionViewResolver {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected static boolean isJavaxValidationImported = false;

    static {
        try {
            Class<?> clazz = Class.forName("javax.validation.ConstraintViolationException");
            if (null != clazz) {
                isJavaxValidationImported = true;
            }
        } catch (ClassNotFoundException ignored) {
        }
    }

    protected boolean logException = true;

    public boolean isLogException() {
        return logException;
    }

    public void setLogException(boolean logException) {
        this.logException = logException;
    }

    protected static int getErrorCode(Exception ex) {
        if (ex != null) {
            if (ex instanceof CodeException) {
                return ((CodeException) ex).getCode();
            }
            if (ex instanceof BindException || (isJavaxValidationImported && ex instanceof ConstraintViolationException)) {
                return 400;
            }
        }
        return 500;
    }

    protected static String getErrorMessage(Exception ex, HttpServletRequest request) {
        if (ex != null) {
            if (isJavaxValidationImported) {
                if (ex instanceof ConstraintViolationException) {
                    ConstraintViolationException exception = (ConstraintViolationException) ex;
                    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
                        return I18nHelper.adapter(request, constraintViolation.getMessage());
                    }
                } else if (ex instanceof BindException) {
                    FieldError fieldError = ((BindException) ex).getFieldError();
                    if (null != fieldError) {
                        return fieldError.getField() + ":" + I18nHelper.adapter(request, fieldError.getDefaultMessage());
                    }
                }
                return I18nHelper.adapter(request, ex.getMessage());
            }
        }
        return I18nHelper.adapter(request, I18nKey.INTERNAL_ERROR.getKey());
    }
}
