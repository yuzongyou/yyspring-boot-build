package com.duowan.yyspringcloud.msauth.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 未认证处理器
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 10:30
 */
@FunctionalInterface
public interface UnauthorizedHandler {

    /**
     * 未认证通过处理器
     *
     * @param securityCode 安全验证码
     * @param request      当前请求
     * @param response     响应
     * @param handler      处理器
     * @throws Exception 任何异常
     */
    void handle(SecurityCode securityCode, HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
