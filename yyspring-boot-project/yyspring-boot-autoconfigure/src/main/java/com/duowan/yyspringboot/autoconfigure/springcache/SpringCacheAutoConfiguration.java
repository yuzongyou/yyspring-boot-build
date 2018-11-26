package com.duowan.yyspringboot.autoconfigure.springcache;

import com.duowan.common.redis.RedisDefinitionContext;
import com.duowan.common.redis.model.RedisDefinition;
import com.duowan.common.redis.model.SentinelRedisDefinition;
import com.duowan.common.redis.model.StdRedisDefinition;
import com.duowan.yyspringboot.autoconfigure.redis.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.Set;

/**
 * SpringCacheAutoConfiguration
 *
 * @author dw_wangdonghong
 * @version V1.0
 * @since 2018/11/22 10:52
 */
@Configuration
@ConditionalOnClass({JedisConnectionFactory.class})
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnProperty(prefix = RedisProperties.PROPERTIES_PREFIX, name = "cache-ids")
@EnableCaching
public class SpringCacheAutoConfiguration implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(SpringCacheAutoConfiguration.class);

    @Autowired
    private RedisProperties redisProperties;
    @Autowired
    private RedisDefinitionContext redisDefinitionContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext;
        doAutoConfiguration(registry);
    }

    protected void doAutoConfiguration(BeanDefinitionRegistry registry) {
        Set<String> cacheIds = redisProperties.getCacheIds();
        String primaryCacheId = redisProperties.getPrimaryCacheId();
        cacheIds.forEach(cacheId -> {
            RedisDefinition redisDefinition = getRedisDefinition(cacheId);
            if (redisDefinition != null) {
                RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(jedisConnectionFactory(cacheId));

                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setBeanClass(YyRedisCacheManager.class);
                beanDefinition.setPrimary(cacheIds.size() == 1 || cacheId.equals(primaryCacheId));
                beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, redisCacheWriter);
                beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(1, getRedisCacheConfiguration());
                registry.registerBeanDefinition(cacheId + "RedisCacheManager", beanDefinition);
            }
        });
    }

    private JedisConnectionFactory jedisConnectionFactory(String cacheId) {
        RedisDefinition redisDefinition = getRedisDefinition(cacheId);
        logger.info("redis缓存配置：{}", redisDefinition.toString());
        JedisConnectionFactory jedisConnectionFactory = null;
        if (redisDefinition instanceof StdRedisDefinition) {
            RedisStandaloneConfiguration standaloneConfig = getStandaloneConfig((StdRedisDefinition) redisDefinition);
            jedisConnectionFactory = new JedisConnectionFactory(standaloneConfig);
        } else if (redisDefinition instanceof SentinelRedisDefinition) {
            RedisSentinelConfiguration sentinelConfig = getRedisSentinelConfiguration((SentinelRedisDefinition) redisDefinition);
            jedisConnectionFactory = new JedisConnectionFactory(sentinelConfig);
        }

        return jedisConnectionFactory;
    }

    private RedisDefinition getRedisDefinition(String cacheId) {
        // 获取数据源配置
        RedisDefinition redisDefinition = redisDefinitionContext.getRedisDefinition(cacheId);

        return redisDefinition;
    }

    private RedisStandaloneConfiguration getStandaloneConfig(StdRedisDefinition stdRedisDefinition) {
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
        standaloneConfig.setPort(Integer.valueOf(stdRedisDefinition.getPort()));
        RedisPassword password = RedisPassword.of(stdRedisDefinition.getPassword());
        standaloneConfig.setPassword(password);
        standaloneConfig.setHostName(stdRedisDefinition.getHost());
        standaloneConfig.setDatabase(Integer.valueOf(stdRedisDefinition.getDatabase()));

        return standaloneConfig;
    }

    private RedisSentinelConfiguration getRedisSentinelConfiguration(SentinelRedisDefinition sentinel) {
        RedisSentinelConfiguration redisSentinelConfiguration =
                new RedisSentinelConfiguration(sentinel.getMasterName(), sentinel.getSentinelSet());
        redisSentinelConfiguration.setDatabase(Integer.valueOf(sentinel.getDatabase()));
        redisSentinelConfiguration.setPassword(RedisPassword.of(sentinel.getPassword()));

        return redisSentinelConfiguration;
    }

    private RedisCacheConfiguration getRedisCacheConfiguration() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer())
                )
                .computePrefixWith(name -> name + ":");

        if (redisProperties.getCacheExpiredTime() > 0) {
            Duration ttl = Duration.ofSeconds(redisProperties.getCacheExpiredTime());
            redisCacheConfiguration = redisCacheConfiguration.entryTtl(ttl);
        }

        return redisCacheConfiguration;
    }


}

