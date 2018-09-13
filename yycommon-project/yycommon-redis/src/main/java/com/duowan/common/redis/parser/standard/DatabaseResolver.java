package com.duowan.common.redis.parser.standard;

import com.duowan.common.redis.model.StdRedisDef;
import com.duowan.common.utils.ConvertUtil;

/**
 * 选择的数据库
 *
 * @author Arvin
 */
public class DatabaseResolver implements StandardRedisDefFieldResolver {

    private static final String KEY_SHORT = "db";
    private static final String KEY_FULL = "database";

    @Override
    public boolean resolve(StdRedisDef redisDef, String key, String subKey, String value) {

        if (!KEY_FULL.equalsIgnoreCase(subKey) && !KEY_SHORT.equalsIgnoreCase(subKey)) {
            return false;
        }

        redisDef.setDatabase(ConvertUtil.toString(value, "0"));

        return true;
    }
}
