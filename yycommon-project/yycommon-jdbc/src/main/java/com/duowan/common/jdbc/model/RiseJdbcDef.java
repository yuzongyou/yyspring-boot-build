package com.duowan.common.jdbc.model;

/**
 * 升龙 Jdbc 定义
 *
 * @author Arvin
 */
public class RiseJdbcDef extends JdbcDef implements Cloneable {

    /**
     * 数据源实例名称，如： cloudapp_cloudmysql1219
     */
    private String name;

    /**
     * 数据库schema
     */
    private String schema;

    /**
     * JDBC URL 参数
     */
    private String jdbcUrlParam = "useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true";

    /**
     * 是否启用从库
     */
    private boolean slaveEnabled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getJdbcUrlParam() {
        return jdbcUrlParam;
    }

    public void setJdbcUrlParam(String jdbcUrlParam) {
        this.jdbcUrlParam = jdbcUrlParam;
    }

    public boolean isSlaveEnabled() {
        return slaveEnabled;
    }

    public void setSlaveEnabled(boolean slaveEnabled) {
        this.slaveEnabled = slaveEnabled;
    }

    public RiseJdbcDef cloneInstance() {
        try {
            return (RiseJdbcDef) super.clone();
        } catch (CloneNotSupportedException e) {

            RiseJdbcDef jdbcDef = new RiseJdbcDef();
            jdbcDef.setName(this.name);
            jdbcDef.setDbType(this.getDbType());
            jdbcDef.setSlaveEnabled(this.isSlaveEnabled());
            jdbcDef.setUsername(this.getUsername());
            jdbcDef.setPassword(this.getPassword());
            jdbcDef.setJdbcUrlParam(this.getJdbcUrlParam());
            jdbcDef.setSchema(this.getSchema());
            jdbcDef.setDriverClass(this.getDriverClass());
            jdbcDef.setDsclazz(this.getDsclazz());
            jdbcDef.setId(this.getId());
            jdbcDef.setInitJdbc(this.isInitJdbc());
            jdbcDef.setSupportTx(this.isSupportTx());
            jdbcDef.setProxyTx(this.isProxyTx());
            jdbcDef.setPoolConfig(this.getPoolConfig());
            jdbcDef.setPoolType(this.getPoolType());
            jdbcDef.setPrimary(this.isPrimary());

            return jdbcDef;

        }
    }
}
