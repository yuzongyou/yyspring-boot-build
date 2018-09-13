package com.duowan.common.jdbc.parser.rise;

import com.duowan.common.jdbc.model.RiseJdbcDef;
import com.duowan.common.utils.ConvertUtil;

import java.util.Map;

/**
 * @author Arvin
 */
public class ProxyTxResolver implements RiseJdbcDefFieldResolver {

    public static final String KEY = "proxyTx";

    @Override
    public boolean resolve(Map<String, String> aliasMap, RiseJdbcDef jdbcDef, String key, String subKey, String value) {

        if (!KEY.equals(subKey)) {
            return false;
        }

        // 默认是支持声明式事务的
        jdbcDef.setProxyTx(ConvertUtil.toBoolean(value, true));

        return true;
    }
}
