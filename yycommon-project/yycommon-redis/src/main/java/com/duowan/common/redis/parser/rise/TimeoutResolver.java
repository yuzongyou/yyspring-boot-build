package com.duowan.common.redis.parser.rise;

import com.duowan.common.redis.model.RiseRedisDef;
import com.duowan.common.utils.ConvertUtil;

import java.util.Map;

/**
 * 超时时间
 *
 * @author Arvin
 */
public class TimeoutResolver implements RiseRedisDefFieldResolver {

    private static final String KEY = "timeout";

    @Override
    public boolean resolve(Map<String, String> aliasMap, RiseRedisDef redisDef, String key, String subKey, String value) {

        if (!KEY.equalsIgnoreCase(subKey)) {
            return false;
        }

        redisDef.setTimeout(String.valueOf(ConvertUtil.toInteger(value, 3000)));

        return true;
    }
}
