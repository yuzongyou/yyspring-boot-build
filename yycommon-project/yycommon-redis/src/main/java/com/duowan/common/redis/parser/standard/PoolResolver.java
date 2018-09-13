package com.duowan.common.redis.parser.standard;

import com.duowan.common.redis.model.StdRedisDef;

import java.util.HashMap;
import java.util.Map;

/**
 * 连接池配置， 属性请看： {@link redis.clients.jedis.JedisPoolConfig}
 *
 * @author Arvin
 */
public class PoolResolver implements StandardRedisDefFieldResolver {

    @Override
    public boolean resolve(StdRedisDef redisDef, String key, String subKey, String value) {

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
