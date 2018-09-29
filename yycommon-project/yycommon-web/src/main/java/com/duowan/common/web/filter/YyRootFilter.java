package com.duowan.common.web.filter;

import com.duowan.common.utils.RequestUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Easy Spring Boot Root Filter
 *
 * @author Arvin
 */
public class YyRootFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(YyRootFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("YyRootFilter init......");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;

            MDC.put(httpServletRequest.getServletPath(), RandomStringUtils.random(10, true, true) + "," + RequestUtil.getClientIp(httpServletRequest));

            HttpServletRequest wrapperRequest = new YyRootWrapper((HttpServletRequest) request, (HttpServletResponse) response);

            chain.doFilter(wrapperRequest, response);
        } finally {
            MDC.clear();
        }

    }

    @Override
    public void destroy() {
        logger.info("YyRootFilter destroy......");
    }

}
