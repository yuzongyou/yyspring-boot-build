package com.duowan.common.jdbc.parser.rise;

import com.duowan.common.jdbc.model.RiseJdbcDef;
import com.duowan.common.utils.ConvertUtil;

import java.util.Map;

/**
 * @author Arvin
 */
public class InitJdbcResolver implements RiseJdbcDefFieldResolver {

    public static final String KEY = "initJdbc";

    @Override
    public boolean resolve(Map<String, String> aliasMap, RiseJdbcDef jdbcDef, String key, String subKey, String value) {

        if (!KEY.equals(subKey)) {
            return false;
        }

        // 默认是会生成 Jdbc 实例
        jdbcDef.setInitJdbc(ConvertUtil.toBoolean(value, true));

        return true;
    }
}
