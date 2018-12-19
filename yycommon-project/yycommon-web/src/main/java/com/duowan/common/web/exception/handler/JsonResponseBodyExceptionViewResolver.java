package com.duowan.common.web.exception.handler;

import com.duowan.common.web.response.JsonResponse;
import com.duowan.common.web.view.StatusJsonView;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 处理默认 JsonResponse
 *
 * @author Arvin
 */
public class JsonResponseBodyExceptionViewResolver extends AbstractExceptionViewResolver {

    public JsonResponseBodyExceptionViewResolver(List<ErrorMessageReader> errorMessageReaderList) {
        super(errorMessageReaderList);
    }

    public JsonResponseBodyExceptionViewResolver(List<ErrorMessageReader> errorMessageReaderList, boolean logException) {
        super(errorMessageReaderList);
        this.setLogException(logException);
    }

    @Override
    public boolean canHandle(HandlerMethod handlerMethod) {
        Class<?> returnType = handlerMethod.getMethod().getReturnType();
        return JsonResponse.class == returnType || JsonResponse.class.isAssignableFrom(returnType);
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) {
        if (ex != null) {
            ErrorMessage errorMessage = getErrorMessage(ex);
            return new StatusJsonView(errorMessage.getErrorCode(), errorMessage.getMessage());
        }

        return null;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 3;
    }
}
