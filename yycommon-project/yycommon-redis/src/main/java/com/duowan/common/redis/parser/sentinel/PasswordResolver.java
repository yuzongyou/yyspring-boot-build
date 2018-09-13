package com.duowan.common.redis.parser.sentinel;

import com.duowan.common.redis.model.SentinelRedisDef;
import com.duowan.common.utils.ConvertUtil;

/**
 * 连接密码
 *
 * @author Arvin
 */
public class PasswordResolver implements SentinelRedisDefFieldResolver {

    private static final String KEY = "password";

    @Override
    public boolean resolve(SentinelRedisDef redisDef, String key, String subKey, String value) {

        if (!KEY.equalsIgnoreCase(subKey)) {
            return false;
        }

        redisDef.setPassword(ConvertUtil.toString(value, null));

        return true;
    }
}
