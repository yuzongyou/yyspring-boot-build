package com.duowan.udb.security.controller;

import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.RequestUtil;
import com.duowan.common.utils.UrlUtil;
import com.duowan.udb.sdk.UdbConstants;
import com.duowan.udb.security.UdbLoginBox;
import com.duowan.udb.security.UdbSecurityConstants;
import com.duowan.udb.security.annotations.IgnoredUdbCheck;
import com.duowan.udb.security.exception.UdbSecurityException;
import com.duowan.udb.util.CookieUtils;
import com.duowan.udb.util.codec.AESHelper;
import com.duowan.universal.login.BasicCredentials;
import com.duowan.universal.login.CallbackServiceParamName;
import com.duowan.universal.login.Credentials;
import com.duowan.universal.login.OAuthHeaderNames;
import com.duowan.universal.login.client.UniversalLoginClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author dw_xiajiqiu1
 */
@Controller
@IgnoredUdbCheck
public class UdbSecurityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdbSecurityController.class);

    private final String udbAppId;
    private final String udbAppKey;

    public UdbSecurityController(String udbAppId, String udbAppKey) {
        if (StringUtils.isBlank(udbAppId) || StringUtils.isBlank(udbAppKey)) {
            this.udbAppId = UdbConstants.DEFAULT_UDB_APPID;
            this.udbAppKey = UdbConstants.DEFAULT_UDB_APPKEY;
        } else {
            this.udbAppId = udbAppId;
            this.udbAppKey = udbAppKey;
        }
    }

    @RequestMapping("/udb/getSdkAuthReq4LayerClose.do")
    public void getSdkAuthReq4LayerClose(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String callbackURL = request.getParameter("callbackURL");
        String denyCallbackURL = request.getParameter("denyCallbackURL");

        PrintWriter out = response.getWriter();
        String json = null;

        try {
            // 提取必要信息，并假定一定存在
            Credentials cc = new BasicCredentials(udbAppId, udbAppKey);
            UniversalLoginClient duowan = new UniversalLoginClient(cc);
            duowan.initialize(callbackURL);
            String tmpTokenSecret = duowan.getTokenSecret();
            tmpTokenSecret = AESHelper.encrypt(tmpTokenSecret, udbAppKey);

            LOGGER.info("entoken:{},detoken:{}", tmpTokenSecret, duowan.getTokenSecret());

            URL redirectURL = duowan.getAuthorizationURL();
            String url = redirectURL.toExternalForm() + "&denyCallbackURL=" + UrlUtil.encodeUrl(denyCallbackURL) + "&UIStyle=qlogin";

            json = String.format("{\"success\": \"1\",\"url\": \"%s\",\"ttokensec\": \"%s\"}", url, tmpTokenSecret);

        } catch (Exception e) {
            LOGGER.info("callbackURL: {}", callbackURL);
            LOGGER.info("denyCallbackURL: {}", denyCallbackURL);
            LOGGER.warn(e.getMessage(), e);
            throw new UdbSecurityException(e);
        } finally {
            LOGGER.info("json={}", json);
            out.append(json);
            out.flush();
            out.close();
        }
    }

    @RequestMapping("/udb/callback.do")
    public void callback(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String url = request.getParameter("url");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.append("<html><head>");

        String cancel = request.getParameter("cancel");
        if (cancel != null) {
            // 被 cancel 时返回前台流
            out.append("<script language=\"JavaScript\" type=\"text/javascript\">");
            out.append("self.parent.UDB.sdk.PCWeb.popupCloseLgn();");
            out.append("</script>");
            out.append("</head><body>");
        } else {
            try {
                // 验证服务端信息，并且返回流数据
                String data = this.validate(url, request);
                out.append(data);
            } catch (Exception e) {
                LOGGER.warn(e.getMessage(), e);
                throw new UdbSecurityException(e);
            }
        }
        out.append("</body></html>");
        out.flush();
        out.close();
    }

    protected String validate(String url, HttpServletRequest request) {

        // 服务端返回的信息
        String oauthToken = request.getParameter(OAuthHeaderNames.TOKEN_KEY);
        String oauthVerifier = request.getParameter(OAuthHeaderNames.VERIFIER);
        String username = request.getParameter(CallbackServiceParamName.USERNAME);

        String tokenSecretCookie = CookieUtils.getCookie(UdbSecurityConstants.COOKIE_UDB_OAUTH_TMP_TOKSN_SEC, request);
        String tokenSecret = null;
        if (StringUtils.isNotBlank(tokenSecretCookie)) {
            tokenSecret = AESHelper.decrypt(tokenSecretCookie, udbAppKey);
        }

        LOGGER.info("[token:{},tokenSecret:{},verifierCode:{},usernmae:{}]",
                oauthToken, tokenSecret, oauthVerifier, username);

        // 校验下
        AssertUtil.assertNotBlank(tokenSecret, "验证出错.tokenSecret为空");
        AssertUtil.assertNotBlank(oauthToken, "验证出错.oauth_token为空");
        AssertUtil.assertNotBlank(oauthVerifier, "验证出错.oauth_verifier为空");

        // 用返回的
        // requestToken以及veriferCode，来做：①换取accessToken；②验证返回信息是否都是合法
        Credentials cc = new BasicCredentials(udbAppId, udbAppKey);
        UniversalLoginClient duowan = new UniversalLoginClient(cc);

        LOGGER.info("oauth_token:{} tokenSecret:{} oauth_verifier:{}", oauthToken, tokenSecret, oauthVerifier);
        String[] accessToken = duowan.getAccessToken(oauthToken, tokenSecret, oauthVerifier);

        String yyuid = duowan.getYyuid(accessToken[0]);
        LOGGER.info("[token:{},tokenSecret:{}]", accessToken[0], accessToken[1]);
        // 存储token信息，udb不负责token信息的存储

        List<String> reqDomainList = new LinkedList<>();
        reqDomainList.add(UniversalLoginClient.CookieDomainEnum.DUOWAN_DOMAIN);
        reqDomainList.add(UniversalLoginClient.CookieDomainEnum.YY_DOMAIN);
        reqDomainList.add(UniversalLoginClient.CookieDomainEnum.KUAIKUAI_DOMAIN);
        reqDomainList.add(UniversalLoginClient.CookieDomainEnum.DOMAIN_HUYA);

        String writeCookieURL = duowan.getWriteCookieURL(accessToken[0], yyuid, reqDomainList);

        LOGGER.info("[writeCookieURL:{}]", writeCookieURL);

        return new StringBuilder().append("<script language=\"JavaScript\" type=\"text/javascript\">function udb_callback(){self.parent.UDB.sdk.PCWeb.writeCrossmainCookieWithCallBack('")
                .append(writeCookieURL)
                .append("',function(){self.parent.document.location.href='")
                .append(url)
                .append("';});};udb_callback();</script>")
                .append("</head><body>")
                .toString();

    }

    @RequestMapping("/udb/logout.do")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        clearSessionInfo(request);

        clearCookieInfo(request, response);

        renderLogoutUI(request, response);
    }

    private void clearSessionInfo(HttpServletRequest request) {

        try {
            HttpSession session = request.getSession();
            session.invalidate();
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(e.getMessage(), e);
            }
        }
    }

    private void clearCookieInfo(HttpServletRequest request, HttpServletResponse response) {

        try {
            String serverName = request.getServerName();
            String topDomain = getTopDomain(serverName);

            Cookie[] cookies = request.getCookies();
            Set<String> cookieNames = new HashSet<>();
            for (Cookie cookie : cookies) {
                // 这个 Cookie 不能删除，删除了就不能自动登录了
                if (UdbSecurityConstants.COOKIE_UDB_OAUTH_TMP_TOKSN_SEC.equals(cookie.getName())) {
                    continue;
                }

                cookieNames.add(cookie.getName());
            }

            if (!cookieNames.isEmpty()) {
                response.addHeader("P3P", "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");

                for (String name : cookieNames) {
                    Cookie cookie = new Cookie(name, "");
                    cookie.setDomain(topDomain);
                    cookie.setPath("/");
                    cookie.setMaxAge(-1);
                    response.addCookie(cookie);
                }
            }
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(e.getMessage(), e);
            }
        }
    }

    private void renderLogoutUI(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String redirectUrl = parseRedirectUrl(request);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.append(UdbLoginBox.getLogoutHtml(udbAppId, udbAppKey, redirectUrl));
        out.flush();
        out.close();
    }

    private String parseRedirectUrl(HttpServletRequest request) {

        String referer = request.getParameter("url");
        if (StringUtils.isEmpty(referer)) {
            referer = RequestUtil.getReferer(request);
        }
        if (StringUtils.isBlank(referer)) {
            referer = RequestUtil.getBasicUrl(request);
        }

        if (referer.contains("/udb/logout.do")) {
            return RequestUtil.getBasicUrl(request);
        }

        // 获取项目根路径
        return referer;
    }

    protected String getTopDomain(String serverName) {
        String topDomain;
        if (serverName.endsWith("yy.com")) {
            topDomain = "yy.com";
        } else if (serverName.endsWith("duowan.com")) {
            topDomain = "duowan.com";
        } else if (serverName.endsWith("kuaikuai.cn")) {
            topDomain = "kuaikuai.cn";
        } else if (serverName.endsWith("huya.com")) {
            topDomain = "huya.com";
        } else {
            throw new IllegalArgumentException("未知域名[" + serverName + "].");
        }
        return topDomain;
    }
}
