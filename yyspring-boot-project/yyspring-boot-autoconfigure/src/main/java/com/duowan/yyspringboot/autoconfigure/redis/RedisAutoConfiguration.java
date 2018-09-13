package com.duowan.yyspringboot.autoconfigure.redis;

import com.duowan.common.jdbc.model.JdbcDef;
import com.duowan.common.redis.Redis;
import com.duowan.common.redis.model.RedisDef;
import com.duowan.common.redis.model.RiseRedisDef;
import com.duowan.common.redis.model.SentinelRedisDef;
import com.duowan.common.redis.model.StdRedisDef;
import com.duowan.common.redis.provider.def.RedisDefProvider;
import com.duowan.common.redis.register.RedisRegister;
import com.duowan.common.redis.register.RedisRegisterUtil;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.ReflectUtil;
import com.duowan.yyspring.boot.AppContext;
import com.duowan.yyspringboot.autoconfigure.jdbc.JdbcProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/13 19:47
 */
@Configuration
@ConditionalOnClass({Redis.class})
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfiguration {

    private Environment environment;

    private RedisProperties redisProperties;

    private BeanDefinitionRegistry registry;

    public RedisAutoConfiguration(Environment environment, RedisProperties redisProperties) {
        this.environment = environment;
        this.redisProperties = redisProperties;
        this.registry = AppContext.getBeanDefinitionRegistry();

        this.doRedisAutoRegister();
    }

    private void doRedisAutoRegister() {

        List<RedisRegister> registerList = newInstances(RedisRegister.class, redisProperties.getRegisterClasses());
        List<RedisDefProvider> providerList = newInstances(RedisDefProvider.class, redisProperties.getRegisterClasses());

        RedisRegisterUtil.registerRedisBeanDefinitions(
                registerList,
                providerList,
                lookupRedisDefList(),
                redisProperties.getEnabledIds(),
                redisProperties.getPrimaryId(),
                registry,
                environment);

    }

    private List<RedisDef> lookupRedisDefList() {

        List<RedisDef> resultList = new ArrayList<>();

        CommonUtil.appendList(resultList, lookupStandardRedisDefList());
        CommonUtil.appendList(resultList, lookupRiseRedisDefList());
        CommonUtil.appendList(resultList, lookupSentinelRedisDefList());

        return resultList;
    }

    private List<RedisDef> lookupStandardRedisDefList() {

        Map<String, StdRedisDef> standardMap = redisProperties.getStandards();
        List<RedisDef> resultList = new ArrayList<>();

        if (null == standardMap || standardMap.isEmpty()) {
            return resultList;
        }

        for (Map.Entry<String, StdRedisDef> entry : standardMap.entrySet()) {
            StdRedisDef def = entry.getValue();
            if (StringUtils.isBlank(def.getId())) {
                def.setId(entry.getKey());
            }
            resultList.add(def);
        }

        return resultList;
    }

    private List<RedisDef> lookupSentinelRedisDefList() {

        Map<String, SentinelRedisDef> standardMap = redisProperties.getSentinels();
        List<RedisDef> resultList = new ArrayList<>();

        if (null == standardMap || standardMap.isEmpty()) {
            return resultList;
        }

        for (Map.Entry<String, SentinelRedisDef> entry : standardMap.entrySet()) {
            SentinelRedisDef def = entry.getValue();
            if (StringUtils.isBlank(def.getId())) {
                def.setId(entry.getKey());
            }
            resultList.add(def);
        }

        return resultList;
    }

    private List<RedisDef> lookupRiseRedisDefList() {

        Map<String, RiseRedisDef> standardMap = redisProperties.getRises();
        List<RedisDef> resultList = new ArrayList<>();

        if (null == standardMap || standardMap.isEmpty()) {
            return resultList;
        }

        Map<String, String> aliasMap = redisProperties.getRiseAlias();

        for (Map.Entry<String, RiseRedisDef> entry : standardMap.entrySet()) {
            RiseRedisDef def = entry.getValue();
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

    private <T> List<T> newInstances(Class<T> requireType, Set<String> classes) {

        if (null == classes || classes.isEmpty()) {
            return new ArrayList<>(0);
        }

        List<T> instanceList = new ArrayList<>();
        for (String className : classes) {
            if (StringUtils.isNotBlank(className)) {
                T instance = ReflectUtil.newInstanceByDefaultConstructor(requireType, className.trim());
                if (null != instance) {
                    instanceList.add(instance);
                }
            }
        }
        return instanceList;
    }
}
