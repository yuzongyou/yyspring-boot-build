package com.duowan.yyspringboot.autoconfigure.web.exception.handler;

import com.duowan.yyspringboot.autoconfigure.web.view.AjaxView;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arvin
 */
@Component
@ConditionalOnMissingBean
public class AjaxViewExceptionViewResolver extends AbstractExceptionViewResolver {

    @Override
    public boolean canHandle(HandlerMethod handlerMethod) {
        Class<?> returnType = handlerMethod.getMethod().getReturnType();
        return AjaxView.class == returnType;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) {

        if (ex != null) {
            int errorCode = getErrorCode(ex);
            String errorMessage = getErrorMessage(ex);
            logger.warn("处理请求异常，返回 AjaxView: status=[" + errorCode + "], errorMessage=[" + errorMessage + "]", ex);
            return new AjaxView(errorCode, errorMessage);
        }

        return null;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 1;
    }
}
