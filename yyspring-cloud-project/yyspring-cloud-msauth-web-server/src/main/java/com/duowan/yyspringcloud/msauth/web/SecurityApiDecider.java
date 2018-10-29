package com.duowan.yyspringcloud.msauth.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 判定是否是
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/10/17 19:48
 */
@FunctionalInterface
public interface SecurityApiDecider {

    /**
     * 是否需要进行安全检查
     *
     * @param request  当前请求
     * @param response 响应对象
     * @param handler  处理类
     * @return 返回是否进行安全拦截
     */
    boolean needSecurityProtected(HttpServletRequest request, HttpServletResponse response, Object handler);
}
