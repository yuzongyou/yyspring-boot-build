package com.duowan.common.jdbc.parser.rise;

import com.duowan.common.jdbc.model.RiseJdbcDef;
import com.duowan.common.utils.ConvertUtil;

import java.util.Map;

/**
 * @author Arvin
 */
public class SlaveResolver implements RiseJdbcDefFieldResolver {

    @Override
    public boolean resolve(Map<String, String> aliasMap, RiseJdbcDef jdbcDef, String key, String subKey, String value) {

        if (!"slave".equalsIgnoreCase(subKey) && !"slaveEnabled".equalsIgnoreCase(subKey)) {
            return false;
        }

        // 默认是会生成 Jdbc 实例
        jdbcDef.setSlaveEnabled(ConvertUtil.toBoolean(value, false));

        return true;
    }
}
