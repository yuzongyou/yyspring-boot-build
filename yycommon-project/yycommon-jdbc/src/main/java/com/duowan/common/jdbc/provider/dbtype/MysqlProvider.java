package com.duowan.common.jdbc.provider.dbtype;

import com.duowan.common.jdbc.MysqlJdbcImpl;
import com.duowan.common.jdbc.model.DBType;
import com.duowan.common.jdbc.model.JdbcDefinition;
import org.springframework.core.env.Environment;

/**
 * Mysql 数据库提供者
 *
 * @author Arvin
 */
public class MysqlProvider implements DBProvider {

    private static final DBType SUPPORT_DB_TYPE = DBType.MYSQL;

    private static final String DEFAULT_DRIVER_CLASS = "com.mysql.jdbc.Driver";

    @Override
    public boolean support(JdbcDefinition jdbcDefinition) {
        return null != jdbcDefinition && SUPPORT_DB_TYPE.equals(jdbcDefinition.getDbType());
    }

    @Override
    public String provideDriverClass() {

        return DEFAULT_DRIVER_CLASS;
    }

    @Override
    public Class<?> lookupJdbcImplClass(JdbcDefinition jdbcDefinition, Environment environment) {
        return MysqlJdbcImpl.class;
    }
}
