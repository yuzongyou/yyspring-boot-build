package com.duowan.yyspringboot.autoconfigure.springcache;

import com.duowan.common.redis.model.RiseRedisDefinition;
import com.duowan.common.redis.model.SentinelRedisDefinition;
import com.duowan.common.redis.model.StdRedisDefinition;
import com.duowan.yyspringboot.autoconfigure.redis.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.Map;

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
@ConditionalOnProperty(prefix = RedisProperties.PROPERTIES_PREFIX, name = "cache-id")
@EnableCaching
public class SpringCacheAutoConfiguration {
    private static Logger logger = LoggerFactory.getLogger(SpringCacheAutoConfiguration.class);

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedisCacheManager redisCacheManager(RedisTemplate redisTemplate) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()))
                .computePrefixWith(name -> name + ":");

        if (redisProperties.getCacheExpiredTime() > 0) {
            Duration ttl = Duration.ofSeconds(redisProperties.getCacheExpiredTime());
            redisCacheConfiguration = redisCacheConfiguration.entryTtl(ttl);
        }

        return new YyRedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }

    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        String cacheId = redisProperties.getCacheId();
        StdRedisDefinition stdRedisDefinition = getRedisDefinition(cacheId, redisProperties);
        if (stdRedisDefinition == null) {
            throw new RuntimeException("请配置缓存redis");
        }
        logger.info("redis缓存配置：{}", stdRedisDefinition.toString());
        RedisStandaloneConfiguration standaloneConfig = getStandaloneConfig(stdRedisDefinition);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(standaloneConfig);

        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate redisTemplate(GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer,
                                       JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(jedisConnectionFactory);

        return redisTemplate;
    }

    private StdRedisDefinition getRedisDefinition(String cacheId, RedisProperties redisProperties) {
        // 获取数据源配置
        StdRedisDefinition stdRedisDefinition = null;
        Map<String, StdRedisDefinition> stdRedisDefinitionMap = redisProperties.getStandards();
        if (stdRedisDefinitionMap != null && stdRedisDefinitionMap.containsKey(cacheId)) {
            stdRedisDefinition = stdRedisDefinitionMap.get(cacheId);
        } else {
            Map<String, RiseRedisDefinition> rises = redisProperties.getRises();
            if (rises != null) {
                stdRedisDefinition = rises.get(cacheId);
            }
        }

        return stdRedisDefinition;
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

    private SentinelRedisDefinition getSentinelRedisDefinition(String cacheId, RedisProperties redisProperties) {
        Map<String, SentinelRedisDefinition> sentinels = redisProperties.getSentinels();
        if (sentinels != null && sentinels.containsKey(cacheId)) {
            return sentinels.get(cacheId);
        }

        return null;
    }

    public RedisSentinelConfiguration getRedisSentinelConfiguration(SentinelRedisDefinition sentinel) {
        RedisSentinelConfiguration redisSentinelConfiguration =
                new RedisSentinelConfiguration(sentinel.getMasterName(), sentinel.getSentinelSet());
        redisSentinelConfiguration.setDatabase(Integer.valueOf(sentinel.getDatabase()));
        redisSentinelConfiguration.setPassword(RedisPassword.of(sentinel.getPassword()));

        return redisSentinelConfiguration;
    }
}

