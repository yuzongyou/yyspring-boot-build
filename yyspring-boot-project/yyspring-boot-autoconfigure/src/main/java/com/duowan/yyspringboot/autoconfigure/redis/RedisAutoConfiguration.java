package com.duowan.yyspringboot.autoconfigure.redis;

import com.duowan.common.redis.Redis;
import com.duowan.common.redis.model.RedisDefinition;
import com.duowan.common.redis.model.RiseRedisDefinition;
import com.duowan.common.redis.model.SentinelRedisDefinition;
import com.duowan.common.redis.model.StdRedisDefinition;
import com.duowan.common.redis.provider.def.RedisDefinitionProvider;
import com.duowan.common.redis.register.RedisRegister;
import com.duowan.common.redis.util.RedisRegisterUtil;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.ReflectUtil;
import com.duowan.yyspringboot.autoconfigure.AbstractAutoConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/13 19:47
 */
@Configuration
@ConditionalOnClass({Redis.class})
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfiguration extends AbstractAutoConfiguration {

    private RedisProperties redisProperties;

    public RedisAutoConfiguration(RedisProperties redisProperties, ApplicationContext applicationContext, Environment environment) {
        super(applicationContext, environment);
        this.redisProperties = redisProperties;

        doAutoConfiguration(applicationContext, getRegistry(), environment);
    }

    protected void doAutoConfiguration(ApplicationContext applicationContext, BeanDefinitionRegistry registry, Environment environment) {
        List<RedisRegister> registerList = ReflectUtil.newInstancesByDefaultConstructor(RedisRegister.class, redisProperties.getRegisterClasses());
        List<RedisDefinitionProvider> providerList = ReflectUtil.newInstancesByDefaultConstructor(RedisDefinitionProvider.class, redisProperties.getRegisterClasses());

        RedisRegisterUtil.registerRedisBeanDefinitions(
                registerList,
                providerList,
                lookupRedisDefList(),
                redisProperties.getEnabledIds(),
                redisProperties.getPrimaryId(),
                registry,
                environment);
    }

    private List<RedisDefinition> lookupRedisDefList() {

        List<RedisDefinition> resultList = new ArrayList<>();

        CommonUtil.appendList(resultList, lookupStandardRedisDefList());
        CommonUtil.appendList(resultList, lookupRiseRedisDefList());
        CommonUtil.appendList(resultList, lookupSentinelRedisDefList());

        return resultList;
    }

    private List<RedisDefinition> lookupStandardRedisDefList() {

        Map<String, StdRedisDefinition> standardMap = redisProperties.getStandards();
        List<RedisDefinition> resultList = new ArrayList<>();

        if (null == standardMap || standardMap.isEmpty()) {
            return resultList;
        }

        for (Map.Entry<String, StdRedisDefinition> entry : standardMap.entrySet()) {
            StdRedisDefinition def = entry.getValue();
            if (StringUtils.isBlank(def.getId())) {
                def.setId(entry.getKey());
            }
            resultList.add(def);
        }

        return resultList;
    }

    private List<RedisDefinition> lookupSentinelRedisDefList() {

        Map<String, SentinelRedisDefinition> standardMap = redisProperties.getSentinels();
        List<RedisDefinition> resultList = new ArrayList<>();

        if (null == standardMap || standardMap.isEmpty()) {
            return resultList;
        }

        for (Map.Entry<String, SentinelRedisDefinition> entry : standardMap.entrySet()) {
            SentinelRedisDefinition def = entry.getValue();
            if (StringUtils.isBlank(def.getId())) {
                def.setId(entry.getKey());
            }
            resultList.add(def);
        }

        return resultList;
    }

    private List<RedisDefinition> lookupRiseRedisDefList() {

        Map<String, RiseRedisDefinition> standardMap = redisProperties.getRises();
        List<RedisDefinition> resultList = new ArrayList<>();

        if (null == standardMap || standardMap.isEmpty()) {
            return resultList;
        }

        Map<String, String> aliasMap = redisProperties.getRiseAlias();

        for (Map.Entry<String, RiseRedisDefinition> entry : standardMap.entrySet()) {
            RiseRedisDefinition def = entry.getValue();
            if (StringUtils.isBlank(def.getId())) {
                def.setId(entry.getKey());
            }
            String dsName = def.getName();
            if (aliasMap != null) {
                String aliasDsName = aliasMap.get(dsName);
                if (StringUtils.isNotBlank(aliasDsName)) {
                    def.setName(aliasDsName);
                }
            }
            resultList.add(def);
        }

        return resultList;
    }

}
