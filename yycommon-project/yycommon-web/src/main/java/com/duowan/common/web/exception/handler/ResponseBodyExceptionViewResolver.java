package com.duowan.common.web.exception.handler;

import com.duowan.common.web.view.JsonView;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理默认 ResponseBody 和 RestController 标注的异常处理方法
 *
 * @author Arvin
 */
public class ResponseBodyExceptionViewResolver extends AbstractExceptionViewResolver {

    public ResponseBodyExceptionViewResolver() {
    }

    public ResponseBodyExceptionViewResolver(boolean logException) {
        this.setLogException(logException);
    }

    @Override
    public boolean canHandle(HandlerMethod handlerMethod) {

        ResponseBody responseBodyAnn = handlerMethod.getMethod().getAnnotation(ResponseBody.class);
        if (null != responseBodyAnn) {
            return true;
        }

        RestController restControllerAnn = handlerMethod.getBeanType().getAnnotation(RestController.class);

        if (null != restControllerAnn) {
            return true;
        }

        return false;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) {
        if (ex != null) {
            int errorCode = getErrorCode(ex);
            String errorMessage = getErrorMessage(ex);
            String logInfo = "处理请求异常，默认返回 JsonView: status=[" + errorCode + "], errorMessage=[" + errorMessage + "]";
            if (logException) {
                logger.warn(logInfo, ex);
            } else {
                logger.warn(logInfo);
            }
            return new JsonView(errorCode, errorMessage);
        }

        return null;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
