package com.duowan.common.redis.parser.sentinel;

import com.duowan.common.redis.model.SentinelRedisDef;
import com.duowan.common.utils.CommonUtil;

/**
 * @author Arvin
 */
public class MasterNameResolver implements SentinelRedisDefFieldResolver {

    private static final String KEY = "masterName";

    @Override
    public boolean resolve(SentinelRedisDef redisDef, String key, String subKey, String value) {

        if (!KEY.equalsIgnoreCase(subKey)) {
            return false;
        }

        redisDef.setMasterName(CommonUtil.trim(value));

        return true;
    }
}
