package com.duowan.common.web.pageparameter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 */
public abstract class AbstractPageParameter implements PageParameter {

    @Override
    public final String getValue(HttpServletRequest request) {

        String name = getName();

        String val = commonGetValue(request, name);
        if (null != val) {
            return val;
        }

        return customGetValue(request);
    }

    public static String commonGetValue(HttpServletRequest request, String name) {
        if (request.getParameterMap().containsKey(name)) {
            return request.getParameter(name);
        }

        Object value = request.getAttribute(name);
        if (value != null) {
            return String.valueOf(value);
        }

        return request.getHeader(name);
    }

    protected abstract String customGetValue(HttpServletRequest request);
}
