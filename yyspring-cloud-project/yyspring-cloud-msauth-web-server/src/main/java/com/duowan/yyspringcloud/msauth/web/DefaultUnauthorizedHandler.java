package com.duowan.yyspringcloud.msauth.web;

import com.duowan.yyspringcloud.msauth.exception.AuthenticationServerErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 10:36
 */
public class DefaultUnauthorizedHandler implements UnauthorizedHandler {

    @Override
    public void handle(SecurityCode securityCode, HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (SecurityCode.SC_UNAUTHORIZED.equals(securityCode)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } else {
            throw new AuthenticationServerErrorException(401, "鉴权失败");
        }
    }
}
