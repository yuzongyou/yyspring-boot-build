package com.duowan.common.web;

import com.duowan.common.utils.RequestUtil;

/**
 * @author xiajiqiu
 * @version 1.0
 * @since 2018/9/9 22:30
 */
public class WebContext {

    private WebContext() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * JSONP 回调函数名称列表
     */
    private static String[] jsonpCallbackVars = new String[]{"callback"};

    /**
     * 时间格式变量
     */
    private static String[] dateFormatVars = new String[]{"dateFormat"};

    /**
     * JavaScript 变量列表
     */
    private static String[] javascriptVars = new String[]{"var"};

    /**
     * 搜索用户IP请求头Header名称列表
     **/
    private static String[] lookupClientIpHeaders = new String[]{"X-Real-IP", "RealIP"};

    /**
     * 获取当前请求协议的Header名称列表
     **/
    private static String[] lookupProtocolHeaders = new String[]{"X-HTTPS-Protocol", "X-Forwarded-Scheme"};

    public static String[] getJsonpCallbackVars() {
        return jsonpCallbackVars;
    }

    public static void setJsonpCallbackVars(String[] jsonpCallbackVars) {
        WebContext.jsonpCallbackVars = jsonpCallbackVars;
    }

    public static String[] getDateFormatVars() {
        return dateFormatVars;
    }

    public static void setDateFormatVars(String[] dateFormatVars) {
        WebContext.dateFormatVars = dateFormatVars;
    }

    public static String[] getJavascriptVars() {
        return javascriptVars;
    }

    public static void setJavascriptVars(String[] javascriptVars) {
        WebContext.javascriptVars = javascriptVars;
    }

    public static String[] getLookupClientIpHeaders() {
        return lookupClientIpHeaders;
    }

    public static void setLookupClientIpHeaders(String[] lookupClientIpHeaders) {
        WebContext.lookupClientIpHeaders = lookupClientIpHeaders;
        RequestUtil.setLookupClientIpHeaders(lookupClientIpHeaders);
    }

    public static String[] getLookupProtocolHeaders() {
        return lookupProtocolHeaders;
    }

    public static void setLookupProtocolHeaders(String[] lookupProtocolHeaders) {
        WebContext.lookupProtocolHeaders = lookupProtocolHeaders;
        RequestUtil.setLookupProtocolHeaders(lookupProtocolHeaders);
    }
}
