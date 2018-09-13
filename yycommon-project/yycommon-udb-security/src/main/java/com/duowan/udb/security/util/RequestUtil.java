package com.duowan.udb.security.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dw_xiajiqiu1
 */
public class RequestUtil {

    public static String getReferer(HttpServletRequest request) {
        return request.getHeader("referer");
    }

    /**
     * 获取用户请求协议，通过 Nginx 请求头： proxy_set_header X-Forwarded-Scheme $scheme
     *
     * @param request 当前请求
     * @return 返回协议
     */
    public static String getProtocol(HttpServletRequest request) {

        // 先计算  X-Forwarded-Scheme 请求头的协议
        String headScheme = request.getHeader("X-Forwarded-Scheme");
        if (StringUtils.isNotBlank(headScheme)) {
            return headScheme + "://";
        }

        String requestUrl = request.getRequestURL().toString();
        return requestUrl.replaceFirst("(?i)^(https?://).*", "$1");
    }

    /**
     * 获取端口参数
     *
     * @param request 请求
     * @return 返回端口和冒号，如果是 80 端口的话直接返回空字符串
     */
    public static String getPortString(HttpServletRequest request) {
        return request.getServerPort() == 80 ? "" : ":" + request.getServerPort();
    }

    /**
     * 获取请求地址URL
     *
     * @param request 当前请求
     * @return 返回能够按照协议区分的请求基础地址，结尾不带 /
     */
    public static String getBasicUrl(HttpServletRequest request) {
        String protocol = getProtocol(request);

        String portStr = getPortString(request);

        return protocol + request.getServerName() + portStr;
    }
}
