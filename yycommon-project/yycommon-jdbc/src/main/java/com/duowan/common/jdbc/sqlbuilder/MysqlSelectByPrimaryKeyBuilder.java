package com.duowan.common.jdbc.sqlbuilder;

import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.JdbcHelper;
import com.duowan.common.jdbc.ModelDef;
import com.duowan.common.jdbc.SqlBuilderType;
import com.duowan.common.jdbc.dialect.Dialect;
import com.duowan.common.jdbc.dialect.MysqlDialect;
import com.duowan.common.utils.AssertUtil;

import java.util.List;

/**
 * @author Arvin
 */
public class MysqlSelectByPrimaryKeyBuilder extends AbstractSqlBuilder<MysqlSelectByPrimaryKeyBuilder> implements SqlBuilder {

    protected static final Dialect DIALECT = new MysqlDialect();

    /** 主键条件值 */
    private final Object[] primaryKeyValues;

    /** 查询sql */
    private String sql;

    public MysqlSelectByPrimaryKeyBuilder(Class<?> modelType, Object... primaryKeyValues) {
        super(modelType, SqlBuilderType.SELECT);
        this.primaryKeyValues = primaryKeyValues;

    }

    private String buildSql() {

        reset();

        ModelDef md = getMd();

        List<FieldDef> pkfdList = md.getPrimaryKeyDefList();

        AssertUtil.assertTrue(pkfdList.size() == primaryKeyValues.length, "定义的主键数量和主键条件数量不一致！");

        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ")
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

        this.addParamsByValues(this.primaryKeyValues);

        sqlBuilder.setLength(sqlBuilder.length() - 5);

        sqlBuilder.append(" LIMIT 1");

        return sqlBuilder.toString().trim();
    }

    @Override
    public String getSql() {
        if (null == this.sql) {
            this.sql = buildSql();
        }
        return this.sql;
    }

    @Override
    protected MysqlSelectByPrimaryKeyBuilder self() {
        return this;
    }

    @Override
    protected String wrapTableName(String tableName) {
        return DIALECT.wrapTableName(tableName);
    }

    @Override
    protected String wrapColumnName(String columnName) {
        return DIALECT.wrapColumnName(columnName);
    }
}
