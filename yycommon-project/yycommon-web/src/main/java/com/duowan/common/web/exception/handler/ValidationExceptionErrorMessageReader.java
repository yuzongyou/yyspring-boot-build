package com.duowan.common.web.exception.handler;

import com.duowan.common.utils.exception.AssertFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/19 10:40
 */
public class ValidationExceptionErrorMessageReader implements ErrorMessageReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationExceptionErrorMessageReader.class);

    protected boolean isJavaxValidationImported = false;

    public ValidationExceptionErrorMessageReader() {
        try {
            Class<?> clazz = Class.forName("javax.validation.ConstraintViolationException");
            if (null != clazz) {
                isJavaxValidationImported = true;
            }
        } catch (ClassNotFoundException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Calculate [isJavaxValidationImported] error: {}", e.getMessage());
            }
        }

    }

    @Override
    public ErrorMessage readErrorMessage(Exception ex) {
        if (null == ex || !isJavaxValidationImported) {
            return new ErrorMessage(false, 500, null, ex);
        }
        int errorCode = 400;
        String errorMessage = "参数验证失败";
        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException exception = (ConstraintViolationException) ex;
            for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
                errorMessage = constraintViolation.getMessage();
            }
            return new ErrorMessage(true, errorCode, errorMessage, ex);
        } else if (ex instanceof BindException) {
            FieldError fieldError = ((BindException) ex).getFieldError();
            assert fieldError != null;
            errorMessage = fieldError.getField() + ":" + fieldError.getDefaultMessage();
            return new ErrorMessage(true, errorCode, errorMessage, ex);
        } else if (ex instanceof IllegalArgumentException || ex instanceof AssertFailException) {
            errorMessage = ex.getMessage();
            return new ErrorMessage(true, errorCode, errorMessage, ex);
        } else {
            return new ErrorMessage(false, 500, null, ex);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 2000;
    }
}
