package com.duowan.yyspringboot.autoconfigure.redis;

import com.duowan.common.redis.model.RedisDefinition;
import com.duowan.common.redis.model.RiseRedisDefinition;
import com.duowan.common.redis.model.SentinelRedisDefinition;
import com.duowan.common.redis.model.StdRedisDefinition;
import com.duowan.common.redis.provider.def.RedisDefinitionProvider;
import com.duowan.common.redis.register.RedisRegister;
import com.duowan.common.redis.util.RedisRegisterUtil;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.ReflectUtil;
import com.duowan.yyspringboot.autoconfigure.SpringApplicationRunListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/26 15:17
 */
public class RedisConfigApplicationRunListener extends SpringApplicationRunListenerAdapter {

    private boolean needAutoConfigurer = false;

    public RedisConfigApplicationRunListener(SpringApplication application, String[] args) {
        super(application, args);
        this.needAutoConfigurer = isClassesImported("com.duowan.common.redis.Redis");
    }

    @Override
    protected boolean needAutoConfigurer() {
        return needAutoConfigurer;
    }

    @Override
    protected void doContextPrepared(ConfigurableApplicationContext context, BeanDefinitionRegistry registry, ConfigurableEnvironment environment) {
        RedisProperties redisProperties = bindProperties(RedisProperties.PROPERTIES_PREFIX, RedisProperties.class);

        if (null != redisProperties) {
            doAutoConfiguration(redisProperties, context, registry, environment);
        }
    }

    protected static void doAutoConfiguration(RedisProperties redisProperties, ApplicationContext applicationContext, BeanDefinitionRegistry registry, Environment environment) {
        List<RedisRegister> registerList = ReflectUtil.newInstancesByDefaultConstructor(RedisRegister.class, redisProperties.getRegisterClasses());
        List<RedisDefinitionProvider> providerList = ReflectUtil.newInstancesByDefaultConstructor(RedisDefinitionProvider.class, redisProperties.getRegisterClasses());

        RedisRegisterUtil.registerRedisBeanDefinitions(
                registerList,
                providerList,
                lookupRedisDefList(redisProperties),
                redisProperties.getEnabledIds(),
                redisProperties.getPrimaryId(),
                registry,
                environment);
    }

    private static List<RedisDefinition> lookupRedisDefList(RedisProperties redisProperties) {

        List<RedisDefinition> resultList = new ArrayList<>();

        CommonUtil.appendList(resultList, lookupStandardRedisDefList(redisProperties));
        CommonUtil.appendList(resultList, lookupRiseRedisDefList(redisProperties));
        CommonUtil.appendList(resultList, lookupSentinelRedisDefList(redisProperties));

        return resultList;
    }

    private static List<RedisDefinition> lookupStandardRedisDefList(RedisProperties redisProperties) {

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

    private static List<RedisDefinition> lookupSentinelRedisDefList(RedisProperties redisProperties) {

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

    private static List<RedisDefinition> lookupRiseRedisDefList(RedisProperties redisProperties) {

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
