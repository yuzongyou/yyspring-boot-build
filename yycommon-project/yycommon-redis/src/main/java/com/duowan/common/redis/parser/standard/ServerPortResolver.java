package com.duowan.common.redis.parser.standard;

import com.duowan.common.redis.model.StdRedisDef;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.CommonUtil;

/**
 * server 和端口属性配置：
 * redis.std.{redisId}.server=host:port
 *
 * @author Arvin
 */
public class ServerPortResolver implements StandardRedisDefFieldResolver {

    private static final String KEY = "server";

    @Override
    public boolean resolve(StdRedisDef redisDef, String key, String subKey, String value) {

        if (!KEY.equalsIgnoreCase(subKey)) {
            return false;
        }

        AssertUtil.assertNotBlank(value, "[" + key + "]对应的Redis 配置值不能为空");

        redisDef.setServer(CommonUtil.trim(value));

        return true;
    }
}
