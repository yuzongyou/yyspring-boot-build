package com.duowan.common.redis.parser.rise;

import com.duowan.common.redis.model.RiseRedisDef;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 升龙数据源实例名称
 *
 * @author Arvin
 * @since 2018/5/23 9:15
 */
public class NameResolver implements RiseRedisDefFieldResolver {

    private static final String KEY = "name";

    @Override
    public boolean resolve(Map<String, String> aliasMap, RiseRedisDef redisDef, String key, String subKey, String value) {

        if (!KEY.equalsIgnoreCase(subKey)) {
            return false;
        }

        String dsName = value;
        String alias = aliasMap.get(value);
        if (StringUtils.isNotBlank(alias)) {
            dsName = alias;
        }

        redisDef.setName(dsName);

        return true;
    }
}
