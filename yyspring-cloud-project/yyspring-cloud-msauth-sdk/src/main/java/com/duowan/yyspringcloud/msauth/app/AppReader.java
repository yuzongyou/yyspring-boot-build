package com.duowan.yyspringcloud.msauth.app;

/**
 * 微服务配置读取
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 9:26
 */
@FunctionalInterface
public interface AppReader {

    /**
     * 搜索APP配置
     *
     * @param appId appId
     * @return 返回APP配置
     */
    App read(String appId);
}
