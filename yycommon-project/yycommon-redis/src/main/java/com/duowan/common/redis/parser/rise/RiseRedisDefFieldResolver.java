package com.duowan.common.redis.parser.rise;

import com.duowan.common.redis.model.RiseRedisDef;

import java.util.Map;

/**
 * @author Arvin
 */
public interface RiseRedisDefFieldResolver {

    /**
     * 处理配置属性
     *
     * @param aliasMap 升龙数据源别名
     * @param redisDef Redis 定义
     * @param key      完整KEY
     * @param subKey   子key
     * @param value    值
     * @return 如果识别并处理了就返回true，否咋返回false
     */
    boolean resolve(Map<String, String> aliasMap, RiseRedisDef redisDef, String key, String subKey, String value);
}
