package com.duowan.yyspringcloud.msauth;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/17 20:03
 */
public interface Constants {

    /**
     * 默认的认证授权HEADER 名称
     */
    String DEFAULT_AUTH_HEADER = "rpc-access-token";

    /**
     * 默认Token appid 分隔符
     */
    char TOKEN_APPID_SEPARATOR = '|';

    String DEFAULT_SECRET_KEY = "default.secret";
    String SECRET_KEY_PREFIX = "secret.";

    int ORDER_APOLLO_APP_READER = 11;

    int ORDER_ENVIRONMENT_APP_READER = 10;
}
