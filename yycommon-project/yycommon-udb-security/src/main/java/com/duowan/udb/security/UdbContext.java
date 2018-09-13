package com.duowan.udb.security;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 */
public class UdbContext {

    private static String appid;
    private static String appkey;

    private UdbContext() {
    }

    public static String getAppid() {
        return appid;
    }

    public static void setAppid(String appid) {
        UdbContext.appid = appid;
    }

    public static String getAppkey() {
        return appkey;
    }

    public static void setAppkey(String appkey) {
        UdbContext.appkey = appkey;
    }

    /**
     * 默认获取的是弱验证
     *
     * @param request 当前请求
     * @return 返回弱验证的对象
     */
    public static UdbOauth getOauth(HttpServletRequest request) {
        return getOauth(request, false);
    }

    public static UdbOauth getOauth(HttpServletRequest request, boolean strongVerify) {
        return new UdbOauth(request, strongVerify);
    }

    /**
     * 获取 弱验证
     *
     * @param request 当前请求对象
     * @return 返回弱验证的 Oauth
     */
    public static UdbOauth getWeakOauth(HttpServletRequest request) {
        return getOauth(request, false);
    }

    /**
     * 获取 强验证
     *
     * @param request 当前请求对象
     * @return 返回强验证的 Oauth
     */
    public static UdbOauth getStrongOauth(HttpServletRequest request) {
        return getOauth(request, true);
    }

    /**
     * 弱登录
     *
     * @param request 当前请求
     * @return 返回是否弱登录，只是进行本地校验
     */
    public static boolean isLoginWeak(HttpServletRequest request) {
        UdbOauth oauth = getWeakOauth(request);
        return null != oauth && oauth.isLogin();
    }

    /**
     * 强登录验证
     *
     * @param request 当前请求
     * @return 返回是否强登录，只是进行本地校验
     */
    public static boolean isLoginStrong(HttpServletRequest request) {
        UdbOauth oauth = getStrongOauth(request);
        return null != oauth && oauth.isLogin();
    }

}
