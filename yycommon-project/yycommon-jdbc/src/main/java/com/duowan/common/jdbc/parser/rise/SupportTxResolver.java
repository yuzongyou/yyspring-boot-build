package com.duowan.common.jdbc.parser.rise;

import com.duowan.common.jdbc.model.RiseJdbcDef;
import com.duowan.common.utils.ConvertUtil;

import java.util.Map;

/**
 * @author Arvin
 * @since 2018/5/16 21:24
 */
public class SupportTxResolver implements RiseJdbcDefFieldResolver {

    public static final String KEY = "supportTx";

    @Override
    public boolean resolve(Map<String, String> aliasMap, RiseJdbcDef jdbcDef, String key, String subKey, String value) {

        if (!KEY.equals(subKey)) {
            return false;
        }

        // 默认不支持
        jdbcDef.setSupportTx(ConvertUtil.toBoolean(value, false));

        return true;
    }
}
