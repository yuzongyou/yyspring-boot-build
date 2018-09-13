package com.duowan.common.jdbc.parser.standard;

import com.duowan.common.jdbc.model.JdbcDef;

/**
 * @author Arvin
 */
public class PasswordResolver implements StandardJdbcDefFieldResolver {

    public static final String KEY = "password";

    @Override
    public boolean resolve(JdbcDef jdbcDef, String key, String subKey, String value) {

        if (!KEY.equals(subKey)) {
            return false;
        }

        jdbcDef.setPassword(value);
        return true;
    }
}
