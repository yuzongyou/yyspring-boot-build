package com.duowan.common.jdbc.sqlbuilder;

import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.JdbcHelper;
import com.duowan.common.jdbc.SqlBuilderType;
import com.duowan.common.jdbc.dialect.Dialect;
import com.duowan.common.jdbc.dialect.MysqlDialect;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.ReflectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arvin
 */
public abstract class AbstractMysqlUpdateBuilder<T> extends AbstractNotQueryBuilder<T> {

    protected static final Dialect DIALECT = new MysqlDialect();

    protected Object model;

    protected AbstractMysqlUpdateBuilder(Object model, SqlBuilderType sqlBuilderType) {
        super(model.getClass(), sqlBuilderType);
        this.model = model;
    }

    @Override
    protected String wrapTableName(String tableName) {
        return DIALECT.wrapTableName(tableName);
    }

    @Override
    protected String wrapColumnName(String columnName) {
        return DIALECT.wrapColumnName(columnName);
    }

    /**
     * 搜索主键， 如果不为空的话就按照主键进行更行
     *
     * @param sqlBuilder sql语句构造器
     * @return 是否进行了更新
     */
    protected boolean lookupAndAppendPrimaryKeyToWhereClause(StringBuilder sqlBuilder) {

        List<FieldDef> pkfdList = getMd().getPrimaryKeyDefList();
        List<SqlParam> sqlParamList = new ArrayList<>();
        if (null != pkfdList && !pkfdList.isEmpty()) {
            StringBuilder primaryKeyWhereBuilder = new StringBuilder(" WHERE ");
            List<Object> primaryKeyValues = new ArrayList<>();
            for (FieldDef fd : pkfdList) {
                Object value = ReflectUtil.getFieldValue(this.model, fd.getField());
                if (JdbcHelper.isValidPrimaryKeyValue(value)) {
                    primaryKeyWhereBuilder.append(wrapColumnName(fd.getColumnName())).append("=? AND");
                    primaryKeyValues.add(value);
                    sqlParamList.add(new SqlParam(fd, value));
                } else {
                    break;
                }
            }
            // 长度相等才能更新
            if (primaryKeyValues.size() == pkfdList.size()) {
                primaryKeyWhereBuilder.setLength(primaryKeyWhereBuilder.length() - 3);
                sqlBuilder.append(primaryKeyWhereBuilder.toString());
                addListSqlParams(sqlParamList);
                return true;
            }
        }

        return false;
    }

    protected void appendUpdateColumns(StringBuilder sqlBuilder, List<FieldDef> needUpdateColumnDefinitions) {
        AssertUtil.assertNotEmpty(needUpdateColumnDefinitions, "没有检测到需要更新的字段！");
        for (FieldDef fd : needUpdateColumnDefinitions) {
            Object newValue = ReflectUtil.getFieldValue(this.model, fd.getField());

            sqlBuilder.append(wrapColumnName(fd.getColumnName())).append("=");

            if (null == newValue) {
                sqlBuilder.append("null,");
            } else {
                sqlBuilder.append("?,");
                addParamByColumnDefinition(fd, newValue);
            }
        }
        // 去掉最后面的 逗号
        sqlBuilder.setLength(sqlBuilder.length() - 1);
    }

}
