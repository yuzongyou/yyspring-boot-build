package com.duowan.common.web.exception.handler;

import com.duowan.common.exception.CodeException;
import org.springframework.core.Ordered;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/19 10:39
 */
public class CodeExceptionErrorMessageReader implements ErrorMessageReader {
    @Override
    public ErrorMessage readErrorMessage(Exception ex) {

        if (ex instanceof CodeException) {
            return new ErrorMessage(true, ((CodeException) ex).getCode(), ex.getMessage(), ex);
        }

        return new ErrorMessage(false, 500, null, ex);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1000;
    }
}
