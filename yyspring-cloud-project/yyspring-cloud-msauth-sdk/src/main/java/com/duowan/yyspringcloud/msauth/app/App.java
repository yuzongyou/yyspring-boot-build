package com.duowan.yyspringcloud.msauth.app;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 9:27
 */
public class App {

    private String appId;
    private String secretKey;

    public App(String appId, String secretKey) {
        this.appId = appId;
        this.secretKey = secretKey;
    }

    public App() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
