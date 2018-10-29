package com.duowan.yyspringcloud.autoconfigure.eureka;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/22 10:28
 */
@ConfigurationProperties(prefix = "yyspring.cloud.eureka.client")
public class EurekaClientProperties {

    /** 使用域名进行注册 **/
    private boolean registerByDomain;

    /** 服务的域名 **/
    private String domain;

    public boolean isRegisterByDomain() {
        return registerByDomain;
    }

    public void setRegisterByDomain(boolean registerByDomain) {
        this.registerByDomain = registerByDomain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
