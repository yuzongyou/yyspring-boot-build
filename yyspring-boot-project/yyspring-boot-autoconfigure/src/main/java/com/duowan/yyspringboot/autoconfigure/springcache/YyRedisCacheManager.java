package com.duowan.yyspringboot.autoconfigure.springcache;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;

/**
 * YyRedisCacheManager 自定义redis缓存管理器，支持给每个cacheName配置缓存时间
 *
 * @author dw_wangdonghong
 * @version V1.0
 * @since 2018/11/22 17:49
 */
public class YyRedisCacheManager extends RedisCacheManager {
    /**
     * 分隔符
     */
    private String separator = "#";
    private RedisCacheConfiguration redisCacheConfiguration;

    public YyRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        redisCacheConfiguration = defaultCacheConfiguration;
    }

    @Override
    protected RedisCache getMissingCache(String cacheName) {
        String[] values = cacheName.split(separator);
        Long expiration = computeExpiration(values);
        if (expiration != null) {
            return createRedisCache(values[0], redisCacheConfiguration.entryTtl(Duration.ofSeconds(expiration)));
        }

        return super.getMissingCache(cacheName);
    }

    private Long computeExpiration(String[] values) {
        if (values.length > 1) {
            return Long.parseLong(values[1]);
        }

        return null;
    }
}
