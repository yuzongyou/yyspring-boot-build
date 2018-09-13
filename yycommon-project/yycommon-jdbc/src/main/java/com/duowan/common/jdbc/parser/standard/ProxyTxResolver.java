package com.duowan.common.jdbc.parser.standard;

import com.duowan.common.jdbc.model.JdbcDef;
import com.duowan.common.utils.ConvertUtil;

/**
 * @author Arvin
 * @since 2018/5/16 21:24
 */
public class ProxyTxResolver implements StandardJdbcDefFieldResolver {

    public static final String KEY = "proxyTx";

    @Override
    public boolean resolve(JdbcDef jdbcDef, String key, String subKey, String value) {

        if (!KEY.equals(subKey)) {
            return false;
        }

        // 默认是支持声明式事务的
        jdbcDef.setProxyTx(ConvertUtil.toBoolean(value, true));

        return true;
    }
}
