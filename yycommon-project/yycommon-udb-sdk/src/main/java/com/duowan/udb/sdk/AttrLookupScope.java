package com.duowan.udb.sdk;

/**
 * 认证属性搜索作用域
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/10/9 20:48
 */
public enum AttrLookupScope {

    /**
     * 请求参数，即 request.getParameter(name)
     **/
    REQUEST,

    /**
     * REQUEST 请求域，即 request.getAttribute(name)
     **/
    ATTRIBUTE,

    /**
     * HEADER 域，即 request.getHeader(name)
     **/
    HEADER,

    /**
     * Cookie 域，即允许从 Cookie 中获取
     **/
    COOKIE,
}
