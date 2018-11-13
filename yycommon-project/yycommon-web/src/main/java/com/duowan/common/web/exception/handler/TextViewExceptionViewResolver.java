package com.duowan.common.web.exception.handler;

import com.duowan.common.web.view.TextView;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arvin
 */
public class TextViewExceptionViewResolver extends AbstractExceptionViewResolver {

    public TextViewExceptionViewResolver() {
    }

    public TextViewExceptionViewResolver(boolean logException) {
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
            int errorCode = getErrorCode(ex);
            String errorMessage = getErrorMessage(ex);
            String logInfo = "处理请求异常，返回 TextView: status=[" + errorCode + "], errorMessage=[" + errorMessage + "]";
            if (logException) {
                logger.warn(logInfo, ex);
            } else {
                logger.warn(logInfo);
            }
            return new TextView(errorCode + ":" + errorMessage);
        }

        return null;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 1;
    }
}
