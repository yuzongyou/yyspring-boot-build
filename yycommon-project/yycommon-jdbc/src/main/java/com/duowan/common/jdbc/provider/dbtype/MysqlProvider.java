package com.duowan.common.jdbc.provider.dbtype;

import com.duowan.common.jdbc.MysqlJdbcImpl;
import com.duowan.common.jdbc.model.DBType;
import com.duowan.common.jdbc.model.JdbcDef;
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
    public boolean support(JdbcDef jdbcDef) {
        return null != jdbcDef && SUPPORT_DB_TYPE.equals(jdbcDef.getDbType());
    }

    @Override
    public String provideDriverClass() {

        return DEFAULT_DRIVER_CLASS;
    }

    @Override
    public Class<?> lookupJdbcImplClass(JdbcDef jdbcDef, Environment environment) {
        return MysqlJdbcImpl.class;
    }
}
