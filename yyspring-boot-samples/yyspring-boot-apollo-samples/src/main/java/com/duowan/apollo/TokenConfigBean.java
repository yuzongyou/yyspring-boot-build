package com.duowan.apollo;


import org.springframework.beans.factory.annotation.Value;

public class TokenConfigBean {

    /**
     * token过期时间，单位秒
     */
    @Value("${qp.token.expireSeconds}")
    private long expireSeconds;

    @Value("${qp.token.version}")
    private String version;

    public long getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(long expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
