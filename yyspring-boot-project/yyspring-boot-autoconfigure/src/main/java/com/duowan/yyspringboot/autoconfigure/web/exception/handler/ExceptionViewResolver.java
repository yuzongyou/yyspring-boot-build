package com.duowan.yyspringboot.autoconfigure.web.exception.handler;

import org.springframework.core.PriorityOrdered;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常视图处理器， 主要处理 TextView, JsonView, AjaxView
 *
 * @author Arvin
 */
public interface ExceptionViewResolver extends PriorityOrdered {

    /**
     * 是否能够处理给定的视图类型
     *
     * @param handlerMethod 请求处理方法
     * @return true 标识能处理该异常请求
     */
    boolean canHandle(HandlerMethod handlerMethod);


    /**
     * 处理异常
     *
     * @param request  请求
     * @param response 响应
     * @param handler  请求处理方法
     * @param ex       异常对象
     * @return 返回 View
     */
    ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex);
}
