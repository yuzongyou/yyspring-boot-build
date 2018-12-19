package com.duowan.common.web.exception.handler;

import com.duowan.common.web.view.AjaxView;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Arvin
 */
public class AjaxViewExceptionViewResolver extends AbstractExceptionViewResolver {

    public AjaxViewExceptionViewResolver(List<ErrorMessageReader> errorMessageReaderList) {
        super(errorMessageReaderList);
    }

    public AjaxViewExceptionViewResolver(List<ErrorMessageReader> errorMessageReaderList, boolean logException) {
        super(errorMessageReaderList);
        this.setLogException(logException);
    }

    @Override
    public boolean canHandle(HandlerMethod handlerMethod) {
        Class<?> returnType = handlerMethod.getMethod().getReturnType();
        return AjaxView.class == returnType || AjaxView.class.isAssignableFrom(returnType);
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) {

        if (ex != null) {
            ErrorMessage errorMessage = getErrorMessage(ex);
            return new AjaxView(errorMessage.getErrorCode(), errorMessage.getMessage());
        }

        return null;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 1;
    }
}
