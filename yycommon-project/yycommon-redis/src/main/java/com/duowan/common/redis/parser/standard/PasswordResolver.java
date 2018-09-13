package com.duowan.common.redis.parser.standard;

import com.duowan.common.redis.model.StdRedisDef;
import com.duowan.common.utils.ConvertUtil;

/**
 * 连接密码
 *
 * @author Arvin
 */
public class PasswordResolver implements StandardRedisDefFieldResolver {

    private static final String KEY = "password";

    @Override
    public boolean resolve(StdRedisDef redisDef, String key, String subKey, String value) {

        if (!KEY.equalsIgnoreCase(subKey)) {
            return false;
        }

        redisDef.setPassword(ConvertUtil.toString(value, null));

        return true;
    }
}
