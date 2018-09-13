package com.duowan.common.redis.parser.sentinel;

import com.duowan.common.redis.model.SentinelRedisDef;

/**
 * @author Arvin
 */
public interface SentinelRedisDefFieldResolver {

    /**
     * 处理配置属性
     *
     * @param redisDef Redis 定义
     * @param key      完整KEY
     * @param subKey   子key
     * @param value    值
     * @return 如果识别并处理了就返回true，否咋返回false
     */
    boolean resolve(SentinelRedisDef redisDef, String key, String subKey, String value);
}
