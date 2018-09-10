package com.duowan.yyspringboot.autoconfigure.web;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Request Util
 *
 * @author Arvin
 */
public abstract class RequestUtil {

    /**
     * 获取客户端 IP 地址， 识别 X-Real-IP, X-Forwarded-For
     *
     * @param request http 请求
     * @return 返回客户端IP
     */
    public static String getClientIp(HttpServletRequest request) {

        String realClientIp = lookupClientIpByHeaders(request);

        if (StringUtils.isBlank(realClientIp)) {
            realClientIp = lookupClientIpByXForwardFor(request);
        }

        if (StringUtils.isBlank(realClientIp)) {
            realClientIp = request.getRemoteAddr();
        }

        return realClientIp;
    }

    private static String lookupClientIpByXForwardFor(HttpServletRequest request) {
        // 检查 "X-Forwarded-For" 需要在Nginx中配置，proxy_set_header X-Forwarded-For $$proxy_add_x_forwarded_for;
        String xforward = request.getHeader("X-Forwarded-For");

        if (StringUtils.isNotBlank(xforward)) {
            String[] forwardIps = xforward.split(",");

            if (forwardIps.length > 0) {
                return forwardIps[0];
            }
        }
        return null;
    }

    private static String lookupClientIpByHeaders(HttpServletRequest request) {
        String[] lookupHeaders = WebContext.getLookupClientIpHeaders();

        String realClientIp = null;
        for (String lookupHeader : lookupHeaders) {
            if (StringUtils.isBlank(realClientIp)) {
                realClientIp = request.getHeader(lookupHeader);
            } else {
                break;
            }
        }
        return realClientIp;
    }

    /**
     * 获取用户请求协议，通过 Nginx 请求头： proxy_set_header X-Forwarded-Scheme $scheme
     *
     * @param request 当前请求
     * @return 返回协议，如http://,https://
     */
    public static String getProtocol(HttpServletRequest request) {
        return getProtocolType(request) + "://";
    }

    /**
     * 获取用户请求协议，通过 Nginx 请求头： proxy_set_header X-Forwarded-Scheme $scheme, proxy_set_header X-HTTPS-Protocol $scheme
     *
     * @param request 当前请求
     * @return 返回协议类型，如http，https
     */
    public static String getProtocolType(HttpServletRequest request) {

        for (String headScheme : WebContext.getLookupProtocolHeaders()) {
            String protocol = request.getHeader(headScheme);
            if (StringUtils.isNotBlank(protocol)) {
                return protocol;
            }
        }
        String requestUrl = request.getRequestURL().toString();
        return requestUrl.replaceFirst("(?i)^(https?)://.*", "$1");
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

    /**
     * 获取请求地址URL
     *
     * @param request      当前请求
     * @param customDomain 使用自己的域名
     * @return 返回能够按照协议区分的请求基础地址，结尾不带 /
     */
    public static String getBasicUrl(HttpServletRequest request, String customDomain) {
        String protocol = getProtocol(request);

        String portStr = getPortString(request);

        String domain = customDomain;
        if (StringUtils.isBlank(domain)) {
            domain = request.getServerName();
        }

        return protocol + domain + portStr;
    }
}
