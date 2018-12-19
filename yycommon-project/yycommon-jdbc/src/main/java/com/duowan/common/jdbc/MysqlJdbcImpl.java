package com.duowan.common.jdbc;

import com.duowan.common.jdbc.dialect.Dialect;
import com.duowan.common.jdbc.dialect.MysqlDialect;
import com.duowan.common.jdbc.sqlbuilder.*;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Arvin
 */
public class MysqlJdbcImpl extends AbstractJdbc {

    private final Dialect dialect = new MysqlDialect();

    @Override
    protected <T> DBExecuteContext buildSelectByPrimaryKeyDBExecuteContext(String tableName, Class<T> modelType, Object[] primaryKeyValues) {

        MysqlSelectByPrimaryKeyBuilder sqlBuilder = new MysqlSelectByPrimaryKeyBuilder(modelType, primaryKeyValues);

        if (StringUtils.isNotBlank(tableName)) {
            sqlBuilder.customTableName(tableName);
        }

        return new DBExecuteContext(sqlBuilder.getSql(), sqlBuilder.getParams());
    }

    @Override
    protected <T> DBExecuteContext buildDeleteByPrimaryKeyDBExecuteContext(String tableName, Class<T> modelType, Object[] primaryKeyValues) {

        MysqlDeleteByPrimaryKeyBuilder sqlBuilder = new MysqlDeleteByPrimaryKeyBuilder(modelType, primaryKeyValues);

        if (StringUtils.isNotBlank(tableName)) {
            sqlBuilder.customTableName(tableName);
        }

        return new DBExecuteContext(sqlBuilder.getSql(), sqlBuilder.getParams());
    }

    @Override
    protected DBExecuteContext buildDeleteByPrimaryKeyDBExecuteContext(String tableName, Object model) {

        MysqlDeleteByPrimaryKeyBuilder sqlBuilder = new MysqlDeleteByPrimaryKeyBuilder(model);

        if (StringUtils.isNotBlank(tableName)) {
            sqlBuilder.customTableName(tableName);
        }

        return new DBExecuteContext(sqlBuilder.getSql(), sqlBuilder.getParams());
    }

    @Override
    protected DBExecuteContext buildUpdateByPrimaryKeyDBExecuteContext(String tableName, Object model) {

        MysqlUpdateByPrimaryKeyBuilder sqlBuilder = new MysqlUpdateByPrimaryKeyBuilder(model);

        if (StringUtils.isNotBlank(tableName)) {
            sqlBuilder.customTableName(tableName);
        }

        return new DBExecuteContext(sqlBuilder.getSql(), sqlBuilder.getParams());
    }

    @Override
    protected DBExecuteContext buildInsertDBExecuteContext(String tableName, Object model) {

        MysqlInsertBuilder sqlBuilder = new MysqlInsertBuilder(model);

        if (StringUtils.isNotBlank(tableName)) {
            sqlBuilder.customTableName(tableName);
        }

        return new DBExecuteContext(sqlBuilder.getSql(), sqlBuilder.getParams());
    }

    @Override
    protected String buildUpdateLimitSql(String sql, int startPageNo, int limit) {
        return dialect.getUpdateLimitSql(sql, startPageNo, limit);
    }

    @Override
    protected String buildPagingSql(String sql, int pageNo, int pageSize) {
        return dialect.getPagingSql(sql, pageNo, pageSize);
    }

    @Override
    protected String buildCountSql(String sql) {
        return dialect.getCountSql(sql);
    }

    @Override
    protected <T> AbstractSelectBuilder createSelectBuilder(Class<T> requiredType, Object queryCondition) {
        return new MysqlSelectBuilder(requiredType, queryCondition);
    }
}
