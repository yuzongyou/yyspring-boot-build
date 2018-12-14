package com.duowan.udb.security;

import com.duowan.universal.login.client.YYSecCenterOpenWSInvoker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Arvin
 */
public class UdbLoginBox {

    private static final String ROOT_CONTEXT_PATH = "/";

    private static final Logger logger = LoggerFactory.getLogger(UdbLoginBox.class);

    /**
     * 获取登录页面文本.
     */
    public static String getLoginBoxHtml(String url, String domain, String contextPath) {
        if (contextPath == null || ROOT_CONTEXT_PATH.equals(contextPath)) {
            contextPath = "";
        }

        String basePath = domain + "" + contextPath;

        String param1 = String.format("document.location.protocol + \"%s/udb/getSdkAuthReq4LayerClose.do\"", basePath);
        String param2 = String.format("document.location.protocol + \"%s/udb/callback.do?xparam=1&url=\" + document.location.protocol + \"%s\"", basePath, url);
        String param3 = String.format("document.location.protocol + \"%s/udb/callback.do?cancel=1\"", basePath);

        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>");
        builder.append("<html lang=\"en\">");
        builder.append("<head>");
        builder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>");
        builder.append("<title>YY统一验证中心</title>");
        builder.append("<script src=\"//res.udb.duowan.com/js/jquery-1.4.2.min.js\" type=\"text/javascript\"></script>");
        builder.append("<script type=\"text/javascript\" src=\"//res.udb.duowan.com/lgn/js/oauth/udbsdk/pcweb/udb.sdk.pcweb.popup.min.js\"></script>");
        builder.append("<script type=\"text/javascript\">");
        builder.append("function showUdbLoginUI() {");
        builder.append("UDB.sdk.PCWeb.popupOpenLgn(").append(param1).append(",").append(param2).append(",").append(param3).append(");");
        builder.append("}");
        builder.append("</script>");
        builder.append("</head>");
        builder.append("<body onload=\"showUdbLoginUI();\"></body>");
        builder.append("</html>");

        return builder.toString();

    }

    /**
     * 获取退出页面文本.
     *
     * @param udbAppId    UDB APPID
     * @param udbAppKey   UDB APPKEY
     * @param redirectUrl 重定向地址
     */
    public static String getLogoutHtml(String udbAppId, String udbAppKey, String redirectUrl) {

        // TODO 这里要验证 redirectUrl 的合法性以及XSS

        String deleteCookieURL = YYSecCenterOpenWSInvoker.getOAuthCookieDeleteURL(udbAppId, udbAppKey);
        StringBuilder builder = new StringBuilder();

        redirectUrl = StringUtils.isBlank(redirectUrl) ? "http://udb.duowan.com/" : redirectUrl;
        logger.info("deleteCookieURL:" + deleteCookieURL);
        logger.info("AfterDeleteCookieURL:" + redirectUrl);
        builder.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        builder.append("<title>YY统一验证中心</title>");
        builder.append("<script src=\"//res.udb.duowan.com/js/jquery-1.4.2.min.js\" type=\"text/javascript\"></script>");
        builder.append("<script src=\"//res.udb.duowan.com/lgn/js/oauth/udbsdk/pcweb/udb.sdk.pcweb.popup.min.js\" type=\"text/javascript\"></script>");
        builder.append("</head>");
        builder.append("<script type=\"text/javascript\">");
        builder.append("function logout(){ UDB.sdk.PCWeb.deleteCrossmainCookieWithCallBack(\"").append(deleteCookieURL).append("\" ,");
        builder.append("					function() { top.location.href = '").append(redirectUrl).append("' } ); }");
        builder.append("</script>");
        builder.append("<body onload=\"logout();\">");
        builder.append("</body>");
        builder.append("</html>");
        return builder.toString();
    }
}
