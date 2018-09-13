package com.duowan.common.jdbc.sqlbuilder;

import com.duowan.common.jdbc.JdbcHelper;
import com.duowan.common.jdbc.SqlBuilderType;
import com.duowan.common.jdbc.dialect.Dialect;
import com.duowan.common.jdbc.dialect.MysqlDialect;
import com.duowan.common.utils.AssertUtil;

import java.util.List;

/**
 * Where 语句构造器
 *
 * @author Arvin
 */
public class MysqlWhereBuilder extends AbstractQueryBuilder<MysqlWhereBuilder> implements WhereBuilder {

    protected static final Dialect DIALECT = new MysqlDialect();

    /**
     * SQL 构造
     */
    private StringBuilder whereBuilder = new StringBuilder();
    /**
     * 条件数量
     */
    private int conditionCount = 0;

    /**
     * 上级Builder
     */
    private WhereBuilder parentBuilder = null;

    public MysqlWhereBuilder(Class<?> modelType) {
        super(modelType, SqlBuilderType.WHERE);
    }

    @Override
    public String buildSql() {
        String sql = build();
        AssertUtil.assertNotBlank(sql, "构建的SQL为空");
        setSql(sql);
        logUsedSql(sql);
        return sql;
    }

    @Override
    protected String build() {
        if (whereBuilder.length() > 0) {
            String sql = whereBuilder.toString();
            if (null == parentBuilder && !sql.matches("(?i) *WHERE.*")) {
                return " WHERE " + whereBuilder.toString();
            } else {
                return whereBuilder.toString();
            }
        }
        return "";
    }

    @Override
    public String getWhereSql() {
        return super.getSql();
    }

    @Override
    public Object[] getArrayParam() {
        return super.getParams();
    }

    @Override
    public List<Object> getListParam() {
        return super.getListParams();
    }

    @Override
    public WhereBuilder setParent(WhereBuilder parentBuilder) {
        this.parentBuilder = parentBuilder;
        return this;
    }

    /**
     * 返回条件数量
     */
    @Override
    public int conditionCount() {
        return conditionCount;
    }


    @Override
    protected MysqlWhereBuilder self() {
        return this;
    }

    @Override
    protected String wrapTableName(String tableName) {
        return DIALECT.wrapTableName(tableName);
    }

    /**
     * 预先适配，如果长度超过一就加AND，同时条件数量自增1
     *
     * @return 返回whereBuilder
     */
    private StringBuilder preAdapter(boolean and) {
        if (whereBuilder.length() > 1) {
            if (and) {
                whereBuilder.append(" AND ");
            } else {
                whereBuilder.append(" OR ");
            }
        }
        ++conditionCount;
        return whereBuilder;
    }

    protected String wrapColumnName(String columnName) {
        return DIALECT.wrapColumnName(columnName);
    }

    @Override
    public WhereBuilder and(String subSql, Object... params) {
        preAdapter(true);
        whereBuilder.append(JdbcHelper.wrapBracketPair(subSql));
        addParamsByValues(params);
        return this;
    }

    @Override
    public WhereBuilder or(String subSql, Object... params) {
        preAdapter(false);
        whereBuilder.append(JdbcHelper.wrapBracketPair(subSql));
        addParamsByValues(params);
        return this;
    }

    @Override
    public WhereBuilder andEqual(String columnName, Object value) {
        if (null == value) {
            return this;
        }
        preAdapter(true).append(wrapColumnName(columnName)).append("=?");
        addParamByColumnName(columnName, value);
        return this;
    }

    @Override
    public WhereBuilder orEqual(String columnName, Object value) {
        if (null == value) {
            return this;
        }
        preAdapter(false).append(wrapColumnName(columnName)).append("=?");
        addParamByColumnName(columnName, value);
        return this;
    }

    @Override
    public WhereBuilder andNotEqual(String columnName, Object value) {
        if (null == value) {
            return this;
        }
        preAdapter(true).append(wrapColumnName(columnName)).append("!=?");
        addParamByColumnName(columnName, value);
        return this;
    }

    @Override
    public WhereBuilder orNotEqual(String columnName, Object value) {
        if (null == value) {
            return this;
        }
        preAdapter(false).append(wrapColumnName(columnName)).append("!=?");
        addParamByColumnName(columnName, value);
        return this;
    }

    @Override
    public WhereBuilder andLike(String columnName, String value) {
        if (null == value) {
            return this;
        }
        preAdapter(true).append(wrapColumnName(columnName)).append(" LIKE CONCAT('%',?,'%')");
        addParamByColumnName(columnName, value);
        return this;
    }

    @Override
    public WhereBuilder orLike(String columnName, String value) {
        if (null == value) {
            return this;
        }
        preAdapter(false).append(wrapColumnName(columnName)).append(" LIKE CONCAT('%',?,'%')");
        addParamByColumnName(columnName, value);
        return this;
    }

    @Override
    public WhereBuilder andIsNull(String columnName) {
        preAdapter(true).append(wrapColumnName(columnName)).append(" IS NULL");
        return this;
    }

    @Override
    public WhereBuilder orIsNull(String columnName) {
        preAdapter(false).append(wrapColumnName(columnName)).append(" IS NULL");
        return this;
    }

    @Override
    public WhereBuilder andIsNullOrBlank(String columnName) {
        columnName = wrapColumnName(columnName);
        preAdapter(true).append("(").append(columnName).append(" IS NULL OR ").append(columnName).append(" = '')");
        return this;
    }

    @Override
    public WhereBuilder orIsNullOrBlank(String columnName) {
        columnName = wrapColumnName(columnName);
        preAdapter(false).append("(").append(columnName).append(" IS NULL OR ").append(columnName).append(" = '')");
        return this;
    }

    @Override
    public WhereBuilder andIsNotNull(String columnName) {
        preAdapter(true).append(wrapColumnName(columnName)).append(" IS NOT NULL");
        return this;
    }

    @Override
    public WhereBuilder orIsNotNull(String columnName) {
        preAdapter(false).append(wrapColumnName(columnName)).append(" IS NOT NULL");
        return this;
    }

    @Override
    public WhereBuilder andIsNotNullOrBlank(String columnName) {
        columnName = wrapColumnName(columnName);
        preAdapter(true).append("(").append(columnName).append(" IS NOT NULL OR ").append(columnName).append(" != '')");
        return this;
    }

    @Override
    public WhereBuilder orIsNotNullOrBlank(String columnName) {
        columnName = wrapColumnName(columnName);
        preAdapter(true).append("(").append(columnName).append(" IS NOT NULL OR ").append(columnName).append(" != '')");
        return this;
    }

    /**
     * Between 语句
     *
     * @param and        是否使用and连接
     * @param columnName 数据库字段名称
     * @param begin      开始
     * @param end        结束
     */
    private WhereBuilder between(boolean and, String columnName, Object begin, Object end) {

        if (null == begin && null == end) {
            return this;
        }

        preAdapter(and);
        String wrapColumnName = wrapColumnName(columnName);

        if (null == begin) {
            whereBuilder.append(wrapColumnName).append("<=?");
            addParamByColumnName(columnName, end);
            return this;
        }

        if (null == end) {
            whereBuilder.append(wrapColumnName).append(">=?");
            addParamByColumnName(columnName, begin);
            return this;
        }

        whereBuilder.append(wrapColumnName).append(" BETWEEN ? AND ?");
        addParamByColumnName(columnName, begin);
        addParamByColumnName(columnName, end);
        return this;
    }

    @Override
    public WhereBuilder andBetween(String columnName, Object begin, Object end) {
        return between(true, columnName, begin, end);
    }

    @Override
    public WhereBuilder orBetween(String columnName, Object begin, Object end) {
        return between(false, columnName, begin, end);
    }

    /**
     * IN 语句查询
     *
     * @param and        是否使用AND查询
     * @param in         是否使用IN，还是使用NOT IN
     * @param columnName 数据库字段
     * @param subSelect  子查询
     * @param params     参数
     */
    private WhereBuilder inOrNot(boolean and, boolean in, String columnName, String subSelect, Object... params) {
        preAdapter(and);
        whereBuilder.append(wrapColumnName(columnName)).append(in ? "" : " NOT").append(" IN (").append(subSelect).append(")");
        addParamsByValues(params);
        return this;
    }

    @Override
    public WhereBuilder andIn(String columnName, String subSelect, Object... params) {
        return inOrNot(true, true, columnName, subSelect, params);
    }

    @Override
    public WhereBuilder orIn(String columnName, String subSelect, Object... params) {
        return inOrNot(false, true, columnName, subSelect, params);
    }

    @Override
    public WhereBuilder andNotIn(String columnName, String subSelect, Object... params) {
        return inOrNot(true, false, columnName, subSelect, params);
    }

    @Override
    public WhereBuilder orNotIn(String columnName, String subSelect, Object... params) {
        return inOrNot(false, false, columnName, subSelect, params);
    }

    /**
     * 大于、大于等于，小于，小于等于
     *
     * @param and        是否AND连接
     * @param lessThan   小于
     * @param equal      是否需要等于
     * @param columnName 数据库字段
     * @param value      参数
     */
    private MysqlWhereBuilder compareOrCompareEqual(boolean and, boolean lessThan, boolean equal, String columnName, Object value) {
        if (null == value) {
            return this;
        }
        preAdapter(and);
        whereBuilder.append(wrapColumnName(columnName)).append(lessThan ? "<" : ">").append(equal ? "=?" : "?");
        addParamByColumnName(columnName, value);
        return this;
    }

    @Override
    public WhereBuilder andGreaterThan(String columnName, Object value, boolean acceptEqual) {
        return compareOrCompareEqual(true, false, acceptEqual, columnName, value);
    }

    @Override
    public WhereBuilder orGreaterThan(String columnName, Object value, boolean acceptEqual) {
        return compareOrCompareEqual(false, false, acceptEqual, columnName, value);
    }

    @Override
    public WhereBuilder andLessThan(String columnName, Object value, boolean acceptEqual) {
        return compareOrCompareEqual(true, true, acceptEqual, columnName, value);
    }

    @Override
    public WhereBuilder orLessThan(String columnName, Object value, boolean acceptEqual) {
        return compareOrCompareEqual(false, true, acceptEqual, columnName, value);
    }

    /**
     * 构造存在或不存在where子句
     *
     * @param and          是否使用AND连接
     * @param exists       是否存在
     * @param subSelectSql 子查询语句
     * @param params       参数
     */
    private WhereBuilder existsOrNot(boolean and, boolean exists, String subSelectSql, Object... params) {
        preAdapter(and);
        whereBuilder.append(exists ? "" : "NOT ").append("EXISTS (").append(subSelectSql).append(")");
        addParamsByValues(params);
        return this;
    }

    @Override
    public WhereBuilder andExists(String subSelect, Object... params) {
        return existsOrNot(true, true, subSelect, params);
    }

    @Override
    public WhereBuilder orExists(String subSelect, Object... params) {
        return existsOrNot(false, true, subSelect, params);
    }

    @Override
    public WhereBuilder andNotExists(String subSelect, Object... params) {
        return existsOrNot(true, false, subSelect, params);
    }

    @Override
    public WhereBuilder orNotExists(String subSelect, Object... params) {
        return existsOrNot(false, false, subSelect, params);
    }
}
