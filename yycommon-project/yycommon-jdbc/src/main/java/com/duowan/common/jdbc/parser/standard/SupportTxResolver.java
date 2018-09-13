package com.duowan.common.jdbc.parser.standard;

import com.duowan.common.jdbc.model.JdbcDef;
import com.duowan.common.utils.ConvertUtil;

/**
 * @author Arvin
 */
public class SupportTxResolver implements StandardJdbcDefFieldResolver {

    public static final String KEY = "supportTx";

    @Override
    public boolean resolve(JdbcDef jdbcDef, String key, String subKey, String value) {

        if (!KEY.equals(subKey)) {
            return false;
        }

        // 默认不支持事务
        jdbcDef.setSupportTx(ConvertUtil.toBoolean(value, false));

        return true;
    }
}
