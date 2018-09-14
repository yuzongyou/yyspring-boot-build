package com.duowan.common.web.pageparameter;

import com.duowan.common.web.RequestUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 */
public class ClientIpPageParameter extends AbstractPageParameter {

    @Override
    public String getName() {
        return "clientIp";
    }

    @Override
    protected String customGetValue(HttpServletRequest request) {
        return RequestUtil.getClientIp(request);
    }
}
