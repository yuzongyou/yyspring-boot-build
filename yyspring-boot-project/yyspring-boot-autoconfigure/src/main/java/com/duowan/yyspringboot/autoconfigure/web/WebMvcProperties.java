package com.duowan.yyspringboot.autoconfigure.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/10 10:19
 */
@ConfigurationProperties(prefix = "yyspring.mvc")
public class WebMvcProperties {

    /**
     * JSONP 回调函数名称列表,默认是 callback
     */
    private String[] jsonpCallbackVars = new String[]{"callback"};

    /**
     * 时间格式变量, 默认是 dateFormat
     */
    private String[] dateFormatVars = new String[]{"dateFormat"};

    /**
     * JavaScript 变量列表，默认是 var
     */
    private String[] javascriptVars = new String[]{"var"};

    /**
     * Ajax 状态码响应字段名称，默认是 status
     **/
    private String ajaxStatusCodeName = "status";

    /**
     * Cookie 中SESSIONID的属性名称,默认是 JSESSIONID
     **/
    private String cookieSessionIdName;

    /**
     * 搜索用户IP请求头Header名称列表,默认是["X-Real-IP", "RealIP"]
     **/
    private String[] lookupClientIpHeaders = new String[]{"X-Real-IP", "RealIP"};

    /**
     * 获取当前请求协议的Header名称列表，默认是["X-HTTPS-Protocol", "X-Forwarded-Scheme"]
     **/
    private String[] lookupProtocolHeaders = new String[]{"X-HTTPS-Protocol", "X-Forwarded-Scheme"};

    public String[] getJsonpCallbackVars() {
        return jsonpCallbackVars;
    }

    public void setJsonpCallbackVars(String[] jsonpCallbackVars) {
        this.jsonpCallbackVars = jsonpCallbackVars;
    }

    public String[] getDateFormatVars() {
        return dateFormatVars;
    }

    public void setDateFormatVars(String[] dateFormatVars) {
        this.dateFormatVars = dateFormatVars;
    }

    public String[] getJavascriptVars() {
        return javascriptVars;
    }

    public void setJavascriptVars(String[] javascriptVars) {
        this.javascriptVars = javascriptVars;
    }

    public String getAjaxStatusCodeName() {
        return ajaxStatusCodeName;
    }

    public void setAjaxStatusCodeName(String ajaxStatusCodeName) {
        this.ajaxStatusCodeName = ajaxStatusCodeName;
    }

    public String getCookieSessionIdName() {
        return cookieSessionIdName;
    }

    public void setCookieSessionIdName(String cookieSessionIdName) {
        this.cookieSessionIdName = cookieSessionIdName;
    }

    public String[] getLookupClientIpHeaders() {
        return lookupClientIpHeaders;
    }

    public void setLookupClientIpHeaders(String[] lookupClientIpHeaders) {
        this.lookupClientIpHeaders = lookupClientIpHeaders;
    }

    public String[] getLookupProtocolHeaders() {
        return lookupProtocolHeaders;
    }

    public void setLookupProtocolHeaders(String[] lookupProtocolHeaders) {
        this.lookupProtocolHeaders = lookupProtocolHeaders;
    }
}
