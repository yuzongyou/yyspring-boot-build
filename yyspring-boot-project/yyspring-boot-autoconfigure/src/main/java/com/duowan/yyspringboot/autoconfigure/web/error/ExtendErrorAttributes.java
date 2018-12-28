package com.duowan.yyspringboot.autoconfigure.web.error;

import com.duowan.common.web.exception.handler.ExceptionViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Arvin
 */
public class ExtendErrorAttributes extends DefaultErrorAttributes implements ErrorAttributes, HandlerExceptionResolver, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.duowan.common.web.exception.ExtendErrorAttributes.class);

    private List<ExceptionViewResolver> exceptionViewResolverList;

    public ExtendErrorAttributes(List<ExceptionViewResolver> exceptionViewResolverList) {
        // 排序
        this.exceptionViewResolverList = sortExceptionViewResolverList(exceptionViewResolverList);

        LOGGER.info("创建异常Attribute: {}", this.getClass().getName());
    }

    /**
     * 排序
     *
     * @param exceptionViewResolverList 要排序的列表
     */
    private List<ExceptionViewResolver> sortExceptionViewResolverList(List<ExceptionViewResolver> exceptionViewResolverList) {

        Collections.sort(exceptionViewResolverList, new Comparator<ExceptionViewResolver>() {
            @Override
            public int compare(ExceptionViewResolver o1, ExceptionViewResolver o2) {
                int ret = o1.getOrder() - o2.getOrder();
                return Integer.compare(ret, 0);
            }
        });

        return exceptionViewResolverList;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mav = super.resolveException(request, response, handler, ex);
        if (null != mav) {
            return mav;
        }

        if (null != ex && handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            for (ExceptionViewResolver exceptionViewResolver : this.exceptionViewResolverList) {
                if (exceptionViewResolver.canHandle(handlerMethod)) {
                    mav = exceptionViewResolver.resolveException(request, response, handlerMethod, ex);
                    if (null != mav) {
                        return mav;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
