package com.duowan.common.jdbc.provider.pooltype;

import com.duowan.common.jdbc.model.JdbcDefinition;
import com.duowan.common.jdbc.model.PoolType;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 9:12
 */
public class C3p0PoolProvider implements PoolProvider {

    private static final PoolType SUPPORT_POOL_TYPE = PoolType.C3P0;

    private static final String DEFAULT_DS_CLASS = "com.mchange.v2.c3p0.ComboPooledDataSource";

    @Override
    public boolean support(JdbcDefinition jdbcDefinition) {
        return null != jdbcDefinition && SUPPORT_POOL_TYPE.equals(jdbcDefinition.getPoolType());
    }

    @Override
    public String provideDsClass() {

        return DEFAULT_DS_CLASS;
    }

    @Override
    public Map<String, String> applyDefaultPoolConfig(Map<String, String> poolConfig) {

        applyDefaultPoolConfig(poolConfig, "preferredTestQuery", "select 1");

        return poolConfig;
    }

    private void applyDefaultPoolConfig(Map<String, String> poolConfig, String fieldName, String defValue) {
        String value = poolConfig.get(fieldName);
        if (StringUtils.isBlank(value) && StringUtils.isNotBlank(defValue)) {
            poolConfig.put(fieldName, defValue);
        }
    }

    @Override
    public String getDriverFieldName() {
        return "driverClass";
    }

    @Override
    public String getUsernameFieldName() {
        return "user";
    }

    @Override
    public String getPasswordFieldName() {
        return "password";
    }

    @Override
    public String getJdbcUrlFieldName() {
        return "jdbcUrl";
    }
}
