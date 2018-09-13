package com.duowan.common.redis.parser.rise;

import com.duowan.common.redis.model.RiseRedisDef;
import com.duowan.common.utils.ConvertUtil;

import java.util.Map;

/**
 * 选择的数据库
 *
 * @author Arvin
 */
public class DatabaseResolver implements RiseRedisDefFieldResolver {

    private static final String KEY_SHORT = "db";
    private static final String KEY_FULL = "database";

    @Override
    public boolean resolve(Map<String, String> aliasMap, RiseRedisDef redisDef, String key, String subKey, String value) {

        if (!KEY_FULL.equalsIgnoreCase(subKey) && !KEY_SHORT.equalsIgnoreCase(subKey)) {
            return false;
        }

        redisDef.setDatabase(ConvertUtil.toString(value, "0"));

        return true;
    }
}
