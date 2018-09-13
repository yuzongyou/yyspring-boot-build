package com.duowan.common.redis.parser.standard;

import com.duowan.common.redis.model.StdRedisDef;
import com.duowan.common.utils.ConvertUtil;

/**
 * 超时时间
 *
 * @author Arvin
 * @since 2018/5/23 9:15
 */
public class TimeoutResolver implements StandardRedisDefFieldResolver {

    private static final String KEY = "timeout";

    @Override
    public boolean resolve(StdRedisDef redisDef, String key, String subKey, String value) {

        if (!KEY.equalsIgnoreCase(subKey)) {
            return false;
        }

        redisDef.setTimeout(String.valueOf(ConvertUtil.toInteger(value, 3000)));

        return true;
    }
}
