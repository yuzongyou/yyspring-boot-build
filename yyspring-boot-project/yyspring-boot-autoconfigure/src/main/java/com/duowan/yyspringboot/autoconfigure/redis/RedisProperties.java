package com.duowan.yyspringboot.autoconfigure.redis;

import com.duowan.common.redis.model.RiseRedisDefinition;
import com.duowan.common.redis.model.SentinelRedisDefinition;
import com.duowan.common.redis.model.StdRedisDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/13 19:41
 */
@ConfigurationProperties(prefix = RedisProperties.PROPERTIES_PREFIX)
public class RedisProperties {

    public static final String PROPERTIES_PREFIX = "yyspring.redis";
    /**
     * 配置启用的 Redis IDS， 中间用英文逗号分隔，如果为空或为配置都会启用所有的 Redis, 允许使用通配符 '*'
     **/
    private Set<String> enabledIds;

    /**
     * 配置禁用的 Redis IDS， 中间用英文逗号分隔，允许使用通配符 '*'
     **/
    private Set<String> excludeIds;

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
    private Map<String, StdRedisDefinition> standards;

    /**
     * 升龙Redis定义
     **/
    private Map<String, RiseRedisDefinition> rises;

    /**
     * 哨兵默认redis定义
     **/
    private Map<String, SentinelRedisDefinition> sentinels;

    /**
     * Redis Bean 注册器类全路径
     **/
    private Set<String> registerClasses;

    /**
     * RedisDefinitionProvider 定义类全路径
     **/
    private Set<String> providerClasses;

    public Set<String> getEnabledIds() {
        return enabledIds;
    }

    public void setEnabledIds(Set<String> enabledIds) {
        this.enabledIds = enabledIds;
    }

    public Set<String> getExcludeIds() {
        return excludeIds;
    }

    public void setExcludeIds(Set<String> excludeIds) {
        this.excludeIds = excludeIds;
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

    public Map<String, StdRedisDefinition> getStandards() {
        return standards;
    }

    public void setStandards(Map<String, StdRedisDefinition> standards) {
        this.standards = standards;
    }

    public Map<String, RiseRedisDefinition> getRises() {
        return rises;
    }

    public void setRises(Map<String, RiseRedisDefinition> rises) {
        this.rises = rises;
    }

    public Map<String, SentinelRedisDefinition> getSentinels() {
        return sentinels;
    }

    public void setSentinels(Map<String, SentinelRedisDefinition> sentinels) {
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
