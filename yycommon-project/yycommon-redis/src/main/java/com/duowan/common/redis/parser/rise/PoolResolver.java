package com.duowan.common.redis.parser.rise;

import com.duowan.common.redis.model.RiseRedisDef;

import java.util.HashMap;
import java.util.Map;

/**
 * 连接池配置， 属性请看： {@link redis.clients.jedis.JedisPoolConfig}
 *
 * @author Arvin
 * @since 2018/5/23 9:22
 */
public class PoolResolver implements RiseRedisDefFieldResolver {


    @Override
    public boolean resolve(Map<String, String> aliasMap, RiseRedisDef redisDef, String key, String subKey, String value) {

        String poolPrefix;

        if (subKey.startsWith("pool.")) {
            poolPrefix = "pool.";
        } else if (subKey.startsWith("poolConfig.")) {
            poolPrefix = "poolConfig.";
        } else {
            return false;
        }

        String poolKey = subKey.replace(poolPrefix, "");

        // 其他属性，放到Map中
        Map<String, String> poolConfig = redisDef.getPoolConfig();
        if (null == poolConfig) {
            poolConfig = new HashMap<>();
            redisDef.setPoolConfig(poolConfig);
        }

        poolConfig.put(poolKey, value);

        return true;
    }
}
