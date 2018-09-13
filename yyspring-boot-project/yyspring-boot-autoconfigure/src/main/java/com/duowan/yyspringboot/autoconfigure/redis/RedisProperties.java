package com.duowan.yyspringboot.autoconfigure.redis;

import com.duowan.common.jdbc.model.JdbcDef;
import com.duowan.common.redis.model.RiseRedisDef;
import com.duowan.common.redis.model.SentinelRedisDef;
import com.duowan.common.redis.model.StdRedisDef;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/13 19:41
 */
@ConfigurationProperties(prefix = "yyspring.redis")
public class RedisProperties {

    /**
     * 配置启用的 Redis IDS， 中间用英文逗号分隔，如果为空或为配置都会启用所有的 Redis, 允许使用通配符 '*'
     **/
    private Set<String> enabledIds;

    /**
     * 一个应用只能有一个 primary 的Jdbc 定义, 默认没有主 Redis
     **/
    private String primaryId;

    /**
     * 升龙数据源别名MAP
     **/
    private Map<String, String> riseAlias;

    /**
     * 标准 Redis 定义
     **/
    private Map<String, StdRedisDef> standards;

    /**
     * 升龙Redis定义
     **/
    private Map<String, RiseRedisDef> rises;

    /**
     * 哨兵默认redis定义
     **/
    private Map<String, SentinelRedisDef> sentinels;

    /**
     * Redis Bean 注册器类全路径
     **/
    private Set<String> registerClasses;

    /**
     * RedisDefProvider 定义类全路径
     **/
    private Set<String> providerClasses;

    public Set<String> getEnabledIds() {
        return enabledIds;
    }

    public void setEnabledIds(Set<String> enabledIds) {
        this.enabledIds = enabledIds;
    }

    public String getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(String primaryId) {
        this.primaryId = primaryId;
    }

    public Map<String, String> getRiseAlias() {
        return riseAlias;
    }

    public void setRiseAlias(Map<String, String> riseAlias) {
        this.riseAlias = riseAlias;
    }

    public Map<String, StdRedisDef> getStandards() {
        return standards;
    }

    public void setStandards(Map<String, StdRedisDef> standards) {
        this.standards = standards;
    }

    public Map<String, RiseRedisDef> getRises() {
        return rises;
    }

    public void setRises(Map<String, RiseRedisDef> rises) {
        this.rises = rises;
    }

    public Map<String, SentinelRedisDef> getSentinels() {
        return sentinels;
    }

    public void setSentinels(Map<String, SentinelRedisDef> sentinels) {
        this.sentinels = sentinels;
    }

    public Set<String> getRegisterClasses() {
        return registerClasses;
    }

    public void setRegisterClasses(Set<String> registerClasses) {
        this.registerClasses = registerClasses;
    }

    public Set<String> getProviderClasses() {
        return providerClasses;
    }

    public void setProviderClasses(Set<String> providerClasses) {
        this.providerClasses = providerClasses;
    }
}
