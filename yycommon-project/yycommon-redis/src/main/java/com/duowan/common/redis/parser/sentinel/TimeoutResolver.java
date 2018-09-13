package com.duowan.common.redis.parser.sentinel;

import com.duowan.common.redis.model.SentinelRedisDef;
import com.duowan.common.utils.ConvertUtil;

/**
 * 超时时间
 *
 * @author Arvin
 */
public class TimeoutResolver implements SentinelRedisDefFieldResolver {

    private static final String KEY = "timeout";

    @Override
    public boolean resolve(SentinelRedisDef redisDef, String key, String subKey, String value) {

        if (!KEY.equalsIgnoreCase(subKey)) {
            return false;
        }

        redisDef.setTimeout(String.valueOf(ConvertUtil.toInteger(value, 3000)));

        return true;
    }
}
