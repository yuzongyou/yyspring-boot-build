package com.duowan.common.redis.parser.rise;

import com.duowan.common.redis.model.RiseRedisDef;
import com.duowan.common.utils.ConvertUtil;

import java.util.Map;

/**
 * 连接密码
 *
 * @author Arvin
 */
public class PasswordResolver implements RiseRedisDefFieldResolver {

    private static final String KEY = "password";

    @Override
    public boolean resolve(Map<String, String> aliasMap, RiseRedisDef redisDef, String key, String subKey, String value) {

        if (!KEY.equalsIgnoreCase(subKey)) {
            return false;
        }

        redisDef.setPassword(ConvertUtil.toString(value, null));

        return true;
    }
}
