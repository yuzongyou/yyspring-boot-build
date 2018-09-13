package com.duowan.common.redis.parser.standard;

import com.duowan.common.redis.model.StdRedisDef;

/**
 * @author Arvin
 */
public interface StandardRedisDefFieldResolver {

    /**
     * 处理配置属性
     *
     * @param redisDef Redis 定义
     * @param key      完整KEY
     * @param subKey   子key
     * @param value    值
     * @return 如果识别并处理了就返回true，否咋返回false
     */
    boolean resolve(StdRedisDef redisDef, String key, String subKey, String value);
}
