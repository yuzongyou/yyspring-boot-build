package com.duowan.yyspringboot.autoconfigure.alarm;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/7 20:01
 */
@ConfigurationProperties(prefix = AlarmProperties.PROPERTIES_PREFIX)
public class AlarmProperties {

    public static final String PROPERTIES_PREFIX = "yyspring.alarm";
    /**
     * 是否需要真的发送告警信息, 默认只有生产环境才会发送
     **/
    private Boolean enabled;

    /**
     * 如果需要自定义警报编号的功能，就需要配置这个，可升龙上配置
     **/
    private String dsnUrl;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDsnUrl() {
        return dsnUrl;
    }

    public void setDsnUrl(String dsnUrl) {
        this.dsnUrl = dsnUrl;
    }
}
