package com.duowan.udb.sdk;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 */
public class UdbContext {

    /**
     * 默认获取的是弱验证
     *
     * @param request 当前请求
     * @return 返回弱验证的对象
     */
    public static UdbOauth getOauth(String udbAppId, String udbAppKey, HttpServletRequest request) {
        return getOauth(udbAppId, udbAppKey, request, UdbAuthLevel.LOCAL);
    }

    public static UdbOauth getOauth(String udbAppId, String udbAppKey, HttpServletRequest request, UdbAuthLevel authLevel) {
        return new UdbOauth(udbAppId, udbAppKey, request, authLevel);
    }

    /**
     * 获取 弱验证
     *
     * @param request 当前请求对象
     * @return 返回弱验证的 Oauth
     */
    public static UdbOauth getWeakOauth(String udbAppId, String udbAppKey, HttpServletRequest request) {
        return getOauth(udbAppId, udbAppKey, request, UdbAuthLevel.LOCAL);
    }

    /**
     * 获取 强验证
     *
     * @param request 当前请求对象
     * @return 返回强验证的 Oauth
     */
    public static UdbOauth getStrongOauth(String udbAppId, String udbAppKey, HttpServletRequest request) {
        return getOauth(udbAppId, udbAppKey, request, UdbAuthLevel.STRONG);
    }

    /**
     * 弱登录
     *
     * @param request 当前请求
     * @return 返回是否弱登录，只是进行本地校验
     */
    public static boolean isLoginWeak(String udbAppId, String udbAppKey, HttpServletRequest request) {
        UdbOauth oauth = getWeakOauth(udbAppId, udbAppKey, request);
        return null != oauth && oauth.isLogin();
    }

    /**
     * 强登录验证
     *
     * @param request 当前请求
     * @return 返回是否强登录，只是进行本地校验
     */
    public static boolean isLoginStrong(String udbAppId, String udbAppKey, HttpServletRequest request) {
        UdbOauth oauth = getStrongOauth(udbAppId, udbAppKey, request);
        return null != oauth && oauth.isLogin();
    }

}
