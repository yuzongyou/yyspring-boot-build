package com.duowan.common.jdbc.parser.rise;

import com.duowan.common.jdbc.model.PoolType;
import com.duowan.common.jdbc.model.RiseJdbcDef;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库连接池属性处理
 *
 * @author Arvin
 * @since 2018/5/16 21:38
 */
public class PoolResolver implements RiseJdbcDefFieldResolver {

    public static final String POOL_PROVIDER = "provider";
    public static final String POOL_CLASS = "dsclazz";

    @Override
    public boolean resolve(Map<String, String> aliasMap, RiseJdbcDef jdbcDef, String key, String subKey, String value) {

        String poolPrefix;

        if (subKey.startsWith("pool.")) {
            poolPrefix = "pool.";
        } else if (subKey.startsWith("poolConfig.")) {
            poolPrefix = "poolConfig.";
        } else {
            return false;
        }

        String poolKey = subKey.replace(poolPrefix, "");

        if (POOL_PROVIDER.equals(poolKey)) {
            jdbcDef.setPoolType(PoolType.parse(value));
            return true;
        }

        if (POOL_CLASS.equals(poolKey)) {
            jdbcDef.setDsclazz(value);
            return true;
        }

        // 其他属性，放到Map中
        Map<String, String> poolConfig = jdbcDef.getPoolConfig();
        if (null == poolConfig) {
            poolConfig = new HashMap<>();
            jdbcDef.setPoolConfig(poolConfig);
        }

        poolConfig.put(poolKey, value);

        return true;
    }
}
