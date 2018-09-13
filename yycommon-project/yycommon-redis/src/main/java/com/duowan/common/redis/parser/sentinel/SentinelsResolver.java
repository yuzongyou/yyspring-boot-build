package com.duowan.common.redis.parser.sentinel;

import com.duowan.common.redis.model.SentinelRedisDef;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.CommonUtil;

/**
 * @author Arvin
 */
public class SentinelsResolver implements SentinelRedisDefFieldResolver {

    public static final String KEY = "sentinels";

    @Override
    public boolean resolve(SentinelRedisDef redisDef, String key, String subKey, String value) {

        if (!KEY.equalsIgnoreCase(subKey)) {
            return false;
        }

        AssertUtil.assertNotBlank(value, "[" + key + "]对应的Redis 配置值不能为空");

        redisDef.setSentinels(CommonUtil.trim(value));

        return true;
    }
}
