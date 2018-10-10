package com.duowan.common.web.filter;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arvin
 */
public class YyRootWrapper extends HttpServletRequestWrapper {

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected static ApplicationContext acx;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request  The request to wrap
     * @param response The response to wrap
     */
    public YyRootWrapper(HttpServletRequest request, HttpServletResponse response) {
        super(request);
        this.request = request;
        this.response = response;

        initContext();
    }

    private void initContext() {
        if (acx == null) {
            acx = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        }
    }

}
