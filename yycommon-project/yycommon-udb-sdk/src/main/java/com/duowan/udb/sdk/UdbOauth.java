package com.duowan.udb.sdk;

import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.ConvertUtil;
import com.duowan.udb.auth.UserinfoForOauth;
import com.duowan.udb.util.CookieUtils;
import com.duowan.universal.login.client.YYSecCenterOpenWSInvoker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import static com.duowan.common.utils.CommonUtil.isAllBlank;

/**
 * UDB 登录Oatuh 认证
 *
 * @author Arvin
 */
public class UdbOauth {

    private static final Logger logger = LoggerFactory.getLogger(UdbOauth.class);

    /**
     * 验证
     */
    private UserinfoForOauth oauth;

    /**
     * 本地验证登录
     */
    private boolean weakLogin;

    /**
     * 是否强验证
     */
    private boolean strongVerify;

    /**
     * 远程强验证登录
     */
    private Boolean strongLogin;

    /**
     * 是否已经登录
     */
    private boolean isLogin;

    /**
     * 用户 YYUID
     */
    private Long yyuid;

    /**
     * 用户通行证
     */
    private String passport;

    private String udbAppId;
    private String udbAppKey;

    public UdbOauth(HttpServletRequest request, boolean strongVerify) {
        this(UdbConstants.DEFAULT_UDB_APPID, UdbConstants.DEFAULT_UDB_APPKEY, request, strongVerify);
    }

    public UdbOauth(String udbAppId, String udbAppKey, HttpServletRequest request, boolean strongVerify) {
        AssertUtil.assertNotNull(request, "UDB 验证，HttpServletRequest 对象不能为空！");

        String oauthCookie = lookupCookieValueExt(request, "oauthCookie");
        String udbOar = lookupCookieValueExt(request, "udb_oar");
        String username = lookupCookieValueExt(request, "username");
        String yyuid = lookupCookieValueExt(request, "yyuid");

        if (isAllBlank(oauthCookie, udbOar, username, yyuid)) {
            init(udbAppId, udbAppKey, new UserinfoForOauth(request, null, udbAppId, udbAppKey), strongVerify);
        } else {
            UserinfoForOauth userinfoForOauth = null;
            if (StringUtils.isNotBlank(username)) {
                userinfoForOauth = new UserinfoForOauth(username, -1, oauthCookie, udbOar, udbAppId, udbAppKey);
            } else if (StringUtils.isNotBlank(yyuid)) {
                userinfoForOauth = new UserinfoForOauth(null, Long.parseLong(yyuid), oauthCookie, udbOar, udbAppId, udbAppKey);
            } else {
                userinfoForOauth = new UserinfoForOauth(request, null, udbAppId, udbAppKey);
            }

            init(udbAppId, udbAppKey, userinfoForOauth, strongVerify);
        }
    }

    private String lookupCookieValueExt(HttpServletRequest request, String ckName) {

        String value = CookieUtils.getCookie(ckName, request);

        if (StringUtils.isBlank(value)) {
            value = request.getHeader(ckName);
        }

        if (StringUtils.isBlank(value)) {
            value = request.getParameter(ckName);
        }

        return value;
    }

    public UdbOauth(String passport, String oauthCookieOrUdbOar, boolean strongVerify) {
        AssertUtil.assertNotBlank(passport, "UDB 验证， 通行证不能为空");
        AssertUtil.assertNotBlank(oauthCookieOrUdbOar, "UDB 验证， 认证Cookie不能为空");

        UserinfoForOauth userinfoForOauth = new UserinfoForOauth(passport, -1, oauthCookieOrUdbOar, oauthCookieOrUdbOar, UdbConstants.DEFAULT_UDB_APPID, UdbConstants.DEFAULT_UDB_APPKEY);
        init(UdbConstants.DEFAULT_UDB_APPID, UdbConstants.DEFAULT_UDB_APPKEY, userinfoForOauth, strongVerify);
    }

    public UdbOauth(String udbAppId, String udbAppKey, String passport, String oauthCookieOrUdbOar, boolean strongVerify) {
        AssertUtil.assertNotBlank(passport, "UDB 验证， 通行证不能为空");
        AssertUtil.assertNotBlank(oauthCookieOrUdbOar, "UDB 验证， 认证Cookie不能为空");

        UserinfoForOauth userinfoForOauth = new UserinfoForOauth(passport, -1, oauthCookieOrUdbOar, oauthCookieOrUdbOar, udbAppId, udbAppKey);
        init(udbAppId, udbAppKey, userinfoForOauth, strongVerify);
    }

    public UdbOauth(String udbAppId, String udbAppKey, long yyuid, String oauthCookieOrUdbOar, boolean strongVerify) {
        AssertUtil.assertTrue(yyuid > 0, "UDB 验证， yyuid 不合法");
        AssertUtil.assertNotBlank(oauthCookieOrUdbOar, "UDB 验证， 认证Cookie不能为空");

        UserinfoForOauth userinfoForOauth = new UserinfoForOauth(null, yyuid, oauthCookieOrUdbOar, oauthCookieOrUdbOar, udbAppId, udbAppKey);
        init(udbAppId, udbAppKey, userinfoForOauth, strongVerify);
    }

    public UdbOauth(long yyuid, String oauthCookieOrUdbOar, boolean strongVerify) {
        AssertUtil.assertTrue(yyuid > 0, "UDB 验证， yyuid 不合法");
        AssertUtil.assertNotBlank(oauthCookieOrUdbOar, "UDB 验证， 认证Cookie不能为空");

        UserinfoForOauth userinfoForOauth = new UserinfoForOauth(null, yyuid, oauthCookieOrUdbOar, oauthCookieOrUdbOar, UdbConstants.DEFAULT_UDB_APPID, UdbConstants.DEFAULT_UDB_APPKEY);
        init(UdbConstants.DEFAULT_UDB_APPID, UdbConstants.DEFAULT_UDB_APPKEY, userinfoForOauth, strongVerify);
    }

    void init(String udbAppId, String udbAppKey, UserinfoForOauth oauth, boolean strongVerify) {
        AssertUtil.assertNotBlank(udbAppId, "UDB 验证， udbAppId 不能为空");
        AssertUtil.assertNotBlank(udbAppKey, "UDB 验证， udbAppKey 不能为空");
        AssertUtil.assertNotNull(oauth, "UDB 验证，UserinfoForOauth 不能为空");

        this.udbAppId = udbAppId;
        this.udbAppKey = udbAppKey;
        this.oauth = oauth;

        this.weakLogin = oauth.validate();
        this.strongVerify = strongVerify;

        if (this.weakLogin) {
            this.yyuid = ConvertUtil.toLong(oauth.getYyuid());
            this.passport = ConvertUtil.toString(oauth.getUsername());

            if (this.strongVerify) {
                this.strongLogin = isStrongLogin();
                this.isLogin = this.strongLogin && this.weakLogin;
            } else {
                this.isLogin = this.weakLogin;
            }
        } else {
            this.isLogin = false;
            this.strongLogin = false;
        }
    }

    /**
     * 是否登录
     *
     * @return true 为已经登录
     */
    public boolean isLogin() {
        return this.isLogin;
    }

    public boolean isWeakLogin() {
        return weakLogin;
    }

    public boolean isStrongVerify() {
        return strongVerify;
    }

    public boolean isStrongLogin() {
        if (this.strongLogin != null) {
            return this.strongLogin;
        }
        try {
            this.strongLogin = YYSecCenterOpenWSInvoker.validAccessTokenByYyuid(udbAppId, udbAppKey,
                    this.oauth.getAccesstoken(), this.oauth.getTokensecret(),
                    this.oauth.getYyuid());
        } catch (Exception e) {
            logger.warn("UDB 强验证失败: passport=" + passport + ",yyuid=" + yyuid + ", error=" + e.getMessage(), e);
            this.strongLogin = false;
        }

        return this.strongLogin;
    }

    public Long getYyuid() {
        return yyuid;
    }

    public String getPassport() {
        return passport;
    }

    public String getUdbAppId() {
        return udbAppId;
    }

    public String getUdbAppKey() {
        return udbAppKey;
    }
}
