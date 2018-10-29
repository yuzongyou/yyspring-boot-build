package com.duowan.yyspringcloud.msauth.web;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/17 20:34
 */
public enum SecurityCode {
    /**
     * 正常的状态码
     **/
    SC_OK,
    /**
     * 认证失败，未授权
     **/
    SC_UNAUTHORIZED,
    /**
     * 其他认证异常
     **/
    SC_ERROR;
}
