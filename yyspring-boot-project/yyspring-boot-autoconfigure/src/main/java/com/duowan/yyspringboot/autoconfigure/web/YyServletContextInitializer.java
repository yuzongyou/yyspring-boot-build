package com.duowan.yyspringboot.autoconfigure.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.ServletContextInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author Arvin
 */
public class YyServletContextInitializer implements ServletContextInitializer {

    /**
     * 会话ID名称，如果不设置默认就是 JESSIONID
     */
    private String sessionIdName;

    public String getSessionIdName() {
        return sessionIdName;
    }

    public void setSessionIdName(String sessionIdName) {
        this.sessionIdName = sessionIdName;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        applyCustomSessionIdName(servletContext);
    }

    /**
     * 应用自定义的 sessionId 名称
     *
     * @param servletContext 上下文
     */
    private void applyCustomSessionIdName(ServletContext servletContext) {

        if (StringUtils.isNotBlank(sessionIdName)) {
            servletContext.getSessionCookieConfig().setName(sessionIdName);
        }
    }
}
