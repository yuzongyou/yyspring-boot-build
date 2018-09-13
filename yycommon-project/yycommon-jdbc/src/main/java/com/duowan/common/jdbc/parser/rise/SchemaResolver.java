package com.duowan.common.jdbc.parser.rise;

import com.duowan.common.jdbc.model.RiseJdbcDef;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 升龙数据源对应的实例名称
 *
 * @author Arvin
 */
public class SchemaResolver implements RiseJdbcDefFieldResolver {

    private static final String KEY = "schema";

    @Override
    public boolean resolve(Map<String, String> aliasMap, RiseJdbcDef jdbcDef, String key, String subKey, String value) {

        if (!KEY.equalsIgnoreCase(subKey)) {
            return false;
        }

        if (StringUtils.isNotBlank(value)) {
            jdbcDef.setSchema(value);
        }

        return true;
    }
}
