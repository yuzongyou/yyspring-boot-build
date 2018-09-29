package com.duowan.common.web.pageparameter;

import com.duowan.common.utils.RequestUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取用户请求协议， 如http , https  通过 Nginx 请求头： X-HTTPS-Protocol，X-Forwarded-Scheme $scheme
 *
 * @author Arvin
 */
public class ProtocolTypePageParameter extends AbstractPageParameter {
    @Override
    public String getName() {
        return "protocolType";
    }

    @Override
    protected String customGetValue(HttpServletRequest request) {
        return RequestUtil.getProtocolType(request);
    }
}
