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
public class TomcatPoolProvider implements PoolProvider {

    private static final PoolType SUPPORT_POOL_TYPE = PoolType.TOMCAT;

    private static final String DEFAULT_DS_CLASS = "org.apache.tomcat.jdbc.pool.DataSource";

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
