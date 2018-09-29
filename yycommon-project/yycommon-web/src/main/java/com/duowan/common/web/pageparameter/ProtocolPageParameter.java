package com.duowan.common.web.pageparameter;

import com.duowan.common.utils.RequestUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取用户请求协议， 如http://, https:// 通过 Nginx 请求头： proxy_set_header X-Forwarded-Scheme $scheme
 *
 * @author Arvin
 */
public class ProtocolPageParameter extends AbstractPageParameter {
    @Override
    public String getName() {
        return "protocol";
    }

    @Override
    protected String customGetValue(HttpServletRequest request) {
        return RequestUtil.getProtocol(request);
    }
}
