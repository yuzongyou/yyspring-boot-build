package com.duowan.yyspringboot.autoconfigure.recachedns;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/7 9:25
 */
@ConfigurationProperties(prefix = "yyspring.recachedns")
public class ReCacheDnsProperties {

    /**
     * DNS的缓存时间, 默认是45秒
     **/
    private int cacheSeconds = 45;

    /**
     * DNS的域名解析预估最大时间, 默认是15秒(15000  毫秒)
     **/
    private int maxDnsMillis = 15000;

    public int getCacheSeconds() {
        return cacheSeconds;
    }

    public void setCacheSeconds(int cacheSeconds) {
        this.cacheSeconds = cacheSeconds;
    }

    public int getMaxDnsMillis() {
        return maxDnsMillis;
    }

    public void setMaxDnsMillis(int maxDnsMillis) {
        this.maxDnsMillis = maxDnsMillis;
    }

}