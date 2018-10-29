package com.duowan.yyspringcloud.msauth.web;

import com.duowan.yyspringcloud.msauth.Constants;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微服务认证拦截器检查
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/10/17 19:42
 */
public class SecurityApiAuthServerHandlerInterceptor implements HandlerInterceptor {

    private final SecurityApiDecider securityApiDecider;

    private final SecurityValidator securityValidator;

    private final UnauthorizedHandler unauthorizedHandler;

    /**
     * 认证HEADER
     **/
    private String authHeader = Constants.DEFAULT_AUTH_HEADER;

    public SecurityApiAuthServerHandlerInterceptor(SecurityApiDecider securityApiDecider, SecurityValidator securityValidator, UnauthorizedHandler unauthorizedHandler) {
        this.securityApiDecider = securityApiDecider == null ? new DefaultSecurityApiDecider() : securityApiDecider;
        if (securityValidator == null) {
            throw new RuntimeException("必须提供安全验证器！");
        }
        this.securityValidator = securityValidator;
        this.unauthorizedHandler = unauthorizedHandler == null ? new DefaultUnauthorizedHandler() : unauthorizedHandler;
    }

    public String getAuthHeader() {
        return authHeader;
    }

    public void setAuthHeader(String authHeader) {
        this.authHeader = authHeader;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean needSecurityProtected = securityApiDecider.needSecurityProtected(request, response, handler);

        if (!needSecurityProtected) {
            return true;
        }

        String authHeaderName = this.authHeader;

        String token = request.getHeader(authHeaderName);
        // 校验Token
        SecurityCode securityCode = this.securityValidator.validate(token, request, handler);
        if (SecurityCode.SC_OK.equals(securityCode)) {
            return true;
        }
        // 认证不通过
        this.unauthorizedHandler.handle(securityCode, request, response, handler);
        return false;
    }
}
