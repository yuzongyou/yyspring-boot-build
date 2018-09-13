package com.duowan.common.jdbc.parser.rise;

import com.duowan.common.jdbc.model.RiseJdbcDef;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 升龙数据源对应的实例名称
 *
 * @author Arvin
 */
public class NameResolver implements RiseJdbcDefFieldResolver {

    private static final String KEY = "name";

    @Override
    public boolean resolve(Map<String, String> aliasMap, RiseJdbcDef jdbcDef, String key, String subKey, String value) {

        if (!KEY.equalsIgnoreCase(subKey)) {
            return false;
        }

        String dsName = value;
        String alias = aliasMap.get(value);
        if (StringUtils.isNotBlank(alias)) {
            dsName = alias;
        }

        jdbcDef.setName(dsName);

        return true;
    }
}
