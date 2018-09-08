package com.duowan.apollo;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author dw_xiajiqiu1
 */
public class Oauth2YyConfig {

    @Value("${get.token.url}")
    private String getTokenUrl;

    @Value("${get.userInfo.url}")
    private String getUserInfoUrl;

    public String getGetTokenUrl() {
        return getTokenUrl;
    }

    public void setGetTokenUrl(String getTokenUrl) {
        this.getTokenUrl = getTokenUrl;
    }

    public String getGetUserInfoUrl() {
        return getUserInfoUrl;
    }

    public void setGetUserInfoUrl(String getUserInfoUrl) {
        this.getUserInfoUrl = getUserInfoUrl;
    }
}
