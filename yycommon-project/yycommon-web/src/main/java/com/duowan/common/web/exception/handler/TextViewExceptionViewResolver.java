package com.duowan.common.web.exception.handler;

import com.duowan.common.web.view.TextView;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Arvin
 */
public class TextViewExceptionViewResolver extends AbstractExceptionViewResolver {

    public TextViewExceptionViewResolver(List<ErrorMessageReader> errorMessageReaderList) {
        super(errorMessageReaderList);
    }

    public TextViewExceptionViewResolver(List<ErrorMessageReader> errorMessageReaderList, boolean logException) {
        super(errorMessageReaderList);
        this.setLogException(logException);
    }

    @Override
    public boolean canHandle(HandlerMethod handlerMethod) {
        Class<?> returnType = handlerMethod.getMethod().getReturnType();
        return TextView.class == returnType || TextView.class.isAssignableFrom(returnType);
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) {

        if (ex != null) {

            ErrorMessage errorMessage = getErrorMessage(ex);
            return new TextView(errorMessage.getErrorCode() + ":" + errorMessage.getMessage());
        }

        return null;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 1;
    }
}
