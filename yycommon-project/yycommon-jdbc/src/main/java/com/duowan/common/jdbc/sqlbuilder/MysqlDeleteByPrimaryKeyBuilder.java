package com.duowan.common.jdbc.sqlbuilder;

import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.JdbcHelper;
import com.duowan.common.jdbc.SqlBuilderType;
import com.duowan.common.jdbc.dialect.Dialect;
import com.duowan.common.jdbc.dialect.MysqlDialect;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.ReflectUtil;

import java.util.List;

/**
 * @author Arvin
 */
public class MysqlDeleteByPrimaryKeyBuilder extends AbstractSqlBuilder<MysqlDeleteByPrimaryKeyBuilder> {

    protected static final Dialect DIALECT = new MysqlDialect();

    /**
     * 主键条件值
     */
    private final Object[] primaryKeyValues;

    /**
     * 查询sql
     */
    private String sql;

    public MysqlDeleteByPrimaryKeyBuilder(Class<?> modelType, Object... primaryKeyValues) {
        this(null, modelType, primaryKeyValues);
    }

    public MysqlDeleteByPrimaryKeyBuilder(String customTableName, Class<?> modelType, Object... primaryKeyValues) {
        super(modelType, SqlBuilderType.DELETE);
        customTableName(customTableName);
        this.primaryKeyValues = primaryKeyValues;
    }

    public MysqlDeleteByPrimaryKeyBuilder(Object model) {
        super(model.getClass(), SqlBuilderType.DELETE);
        this.primaryKeyValues = getPrimaryKeyValues(model);
    }

    @Override
    protected String wrapTableName(String tableName) {
        return DIALECT.wrapTableName(tableName);
    }

    @Override
    protected String wrapColumnName(String columnName) {
        return DIALECT.wrapColumnName(columnName);
    }

    private Object[] getPrimaryKeyValues(Object model) {

        List<FieldDef> pkfdList = getMd().getPrimaryKeyDefList();

        int size = pkfdList.size();

        Object[] pkValues = new Object[size];

        for (int i = 0; i < size; ++i) {
            pkValues[i] = ReflectUtil.getFieldValue(model, pkfdList.get(i).getField());
        }

        return pkValues;
    }


    private String buildSql() {

        reset();

        List<FieldDef> pkfdList = getMd().getPrimaryKeyDefList();

        AssertUtil.assertTrue(pkfdList.size() == primaryKeyValues.length, "定义的主键数量和主键条件数量不一致！");

        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM ")
                .append(getWrapTableName())
                .append(" WHERE ");

        for (int i = 0; i < pkfdList.size(); ++i) {

            FieldDef fd = pkfdList.get(i);
            Object value = primaryKeyValues[i];

            AssertUtil.assertNotNull(value, "第[" + (i + 1) + "]个主键不能为空！FOR DELETE");

            AssertUtil.assertTrue(JdbcHelper.isTypeMatch(fd.getFieldType(), value.getClass()),
                    "主键类型和给定的值不匹配，第[" + (i + 1) + "]个主键类型为[" + fd.getFieldType().getName() + "], 传入的值类型为[" + value.getClass().getName() + "]");

            sqlBuilder.append(DIALECT.wrapColumnName(fd.getColumnName())).append("=? AND ");
        }

        addParamsByValues(primaryKeyValues);

        sqlBuilder.setLength(sqlBuilder.length() - 5);

        sqlBuilder.append(" LIMIT 1");

        return sqlBuilder.toString().trim();
    }

    public String getSql() {
        if (sql == null) {
            this.sql = buildSql();
        }
        return this.sql;
    }

    @Override
    protected MysqlDeleteByPrimaryKeyBuilder self() {
        return this;
    }
}
