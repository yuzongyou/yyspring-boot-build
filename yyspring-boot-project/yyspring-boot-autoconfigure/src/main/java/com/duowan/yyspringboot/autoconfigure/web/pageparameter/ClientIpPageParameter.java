package com.duowan.yyspringboot.autoconfigure.web.pageparameter;

import com.duowan.yyspringboot.autoconfigure.web.RequestUtil;

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
