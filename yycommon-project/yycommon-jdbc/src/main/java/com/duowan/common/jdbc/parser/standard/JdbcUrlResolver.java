package com.duowan.common.jdbc.parser.standard;

import com.duowan.common.jdbc.model.JdbcDef;

/**
 * @author Arvin
 */
public class JdbcUrlResolver implements StandardJdbcDefFieldResolver {

    public static final String KEY = "jdbcUrl";

    @Override
    public boolean resolve(JdbcDef jdbcDef, String key, String subKey, String value) {

        if (!KEY.equals(subKey)) {
            return false;
        }

        // 默认不是主
        jdbcDef.setJdbcUrl(value);

        return true;
    }
}
