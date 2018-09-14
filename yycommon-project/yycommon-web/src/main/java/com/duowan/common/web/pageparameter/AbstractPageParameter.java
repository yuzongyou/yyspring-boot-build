package com.duowan.common.web.pageparameter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 */
public abstract class AbstractPageParameter implements PageParameter {

    @Override
    public final String getValue(HttpServletRequest request) {

        String name = getName();

        if (request.getParameterMap().containsKey(name)) {
            return request.getParameter(name);
        }

        Object value = request.getAttribute(name);
        if (value != null) {
            return String.valueOf(value);
        }

        return customGetValue(request);
    }

    protected abstract String customGetValue(HttpServletRequest request);
}
