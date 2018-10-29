package com.duowan.yyspringcloud.msauth.web;

import javax.servlet.http.HttpServletRequest;

/**
 * API 接口 Token 验证
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/10/17 20:29
 */
@FunctionalInterface
public interface SecurityValidator {

    /**
     * 验证Token
     *
     * @param token   token
     * @param request 请求
     * @param handler 处理器
     * @return 返回安全code
     */
    SecurityCode validate(String token, HttpServletRequest request, Object handler);
}
