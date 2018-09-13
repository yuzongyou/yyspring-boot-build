package com.duowan.common.redis.parser.sentinel;

import com.duowan.common.redis.model.SentinelRedisDef;
import com.duowan.common.utils.ConvertUtil;

/**
 * 选择的数据库
 *
 * @author Arvin
 */
public class DatabaseResolver implements SentinelRedisDefFieldResolver {

    private static final String KEY_SHORT = "db";
    private static final String KEY_FULL = "database";

    @Override
    public boolean resolve(SentinelRedisDef redisDef, String key, String subKey, String value) {

        if (!KEY_FULL.equalsIgnoreCase(subKey) && !KEY_SHORT.equalsIgnoreCase(subKey)) {
            return false;
        }

        redisDef.setDatabase(ConvertUtil.toString(value, "0"));

        return true;
    }
}
