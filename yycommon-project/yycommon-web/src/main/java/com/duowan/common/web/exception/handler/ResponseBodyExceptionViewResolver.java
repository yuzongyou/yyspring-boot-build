package com.duowan.common.web.exception.handler;

import com.duowan.common.web.view.JsonView;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 处理默认 ResponseBody 和 RestController 标注的异常处理方法
 *
 * @author Arvin
 */
public class ResponseBodyExceptionViewResolver extends AbstractExceptionViewResolver {

    public ResponseBodyExceptionViewResolver(List<ErrorMessageReader> errorMessageReaderList) {
        super(errorMessageReaderList);
    }

    public ResponseBodyExceptionViewResolver(List<ErrorMessageReader> errorMessageReaderList, boolean logException) {
        super(errorMessageReaderList);
        this.setLogException(logException);
    }

    @Override
    public boolean canHandle(HandlerMethod handlerMethod) {

        ResponseBody responseBodyAnn = handlerMethod.getMethod().getAnnotation(ResponseBody.class);
        if (null != responseBodyAnn) {
            return true;
        }

        RestController restControllerAnn = handlerMethod.getBeanType().getAnnotation(RestController.class);

        return null != restControllerAnn;

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
        return Integer.MAX_VALUE;
    }
}
