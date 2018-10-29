package com.duowan.common.web.exception.handler;

import com.duowan.common.web.response.JsonResponse;
import com.duowan.common.web.view.StatusJsonView;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理默认 JsonResponse
 *
 * @author Arvin
 */
public class JsonResponseBodyExceptionViewResolver extends AbstractExceptionViewResolver {

    @Override
    public boolean canHandle(HandlerMethod handlerMethod) {
        Class<?> returnType = handlerMethod.getMethod().getReturnType();
        return JsonResponse.class == returnType || JsonResponse.class.isAssignableFrom(returnType);
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) {
        if (ex != null) {
            int errorCode = getErrorCode(ex);
            String errorMessage = getErrorMessage(ex);
            logger.warn("处理请求异常，默认返回 JsonResponse: status=[" + errorCode + "], errorMessage=[" + errorMessage + "]", ex);
            return new StatusJsonView(errorCode, errorMessage);
        }

        return null;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 3;
    }
}
