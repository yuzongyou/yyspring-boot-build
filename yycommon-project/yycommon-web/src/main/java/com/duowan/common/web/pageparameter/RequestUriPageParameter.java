package com.duowan.common.web.pageparameter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 */
public class RequestUriPageParameter extends AbstractPageParameter {
    @Override
    public String getName() {
        return "requestUri";
    }

    @Override
    protected String customGetValue(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
