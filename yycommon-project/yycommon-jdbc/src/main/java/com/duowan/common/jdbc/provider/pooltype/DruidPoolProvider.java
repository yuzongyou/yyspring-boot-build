package com.duowan.common.jdbc.provider.pooltype;

import com.duowan.common.jdbc.model.JdbcDef;
import com.duowan.common.jdbc.model.PoolType;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Druid 数据库连接池提供者
 *
 * @author Arvin
 */
public class DruidPoolProvider implements PoolProvider {

    private static final PoolType SUPPORT_POOL_TYPE = PoolType.DRUID;

    private static final String DEFAULT_DS_CLASS = "com.alibaba.druid.pool.DruidDataSource";

    @Override
    public boolean support(JdbcDef jdbcDef) {
        return null != jdbcDef && SUPPORT_POOL_TYPE.equals(jdbcDef.getPoolType());
    }

    @Override
    public String provideDsClass() {

        return DEFAULT_DS_CLASS;
    }

    @Override
    public Map<String, String> applyDefaultPoolConfig(Map<String, String> poolConfig) {

        applyDefaultPoolConfig(poolConfig, "poolPreparedStatements", "false");
        applyDefaultPoolConfig(poolConfig, "validationQuery", "select 1");
        applyDefaultPoolConfig(poolConfig, "testWhileIdle", "true");

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
        return "driverClassName";
    }

    @Override
    public String getUsernameFieldName() {
        return "username";
    }

    @Override
    public String getPasswordFieldName() {
        return "password";
    }

    @Override
    public String getJdbcUrlFieldName() {
        return "url";
    }
}
