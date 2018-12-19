package com.duowan.common.web.exception.handler;

import com.duowan.common.web.view.JsonView;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Arvin
 */
public class JsonViewExceptionViewResolver extends AbstractExceptionViewResolver {

    public JsonViewExceptionViewResolver(List<ErrorMessageReader> errorMessageReaderList) {
        super(errorMessageReaderList);
    }

    public JsonViewExceptionViewResolver(List<ErrorMessageReader> errorMessageReaderList, boolean logException) {
        super(errorMessageReaderList);
        this.setLogException(logException);
    }

    @Override
    public boolean canHandle(HandlerMethod handlerMethod) {
        Class<?> returnType = handlerMethod.getMethod().getReturnType();
        return JsonView.class == returnType || JsonView.class.isAssignableFrom(returnType);
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) {

        if (ex != null) {

            ErrorMessage errorMessage = getErrorMessage(ex);
            return new JsonView(errorMessage.getErrorCode(), errorMessage.getMessage());
        }

        return null;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 2;
    }
}
