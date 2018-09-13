package com.duowan.common.jdbc.parser.standard;

import com.duowan.common.jdbc.model.JdbcDef;
import com.duowan.common.utils.ConvertUtil;

/**
 * @author Arvin
 * @since 2018/5/16 21:24
 */
public class InitJdbcResolver implements StandardJdbcDefFieldResolver {

    public static final String KEY = "initJdbc";

    @Override
    public boolean resolve(JdbcDef jdbcDef, String key, String subKey, String value) {

        if (!KEY.equals(subKey)) {
            return false;
        }

        // 默认是会生成 Jdbc 实例
        jdbcDef.setInitJdbc(ConvertUtil.toBoolean(value, true));

        return true;
    }
}
