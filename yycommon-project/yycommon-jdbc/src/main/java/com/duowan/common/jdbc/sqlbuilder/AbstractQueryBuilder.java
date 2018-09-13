package com.duowan.common.jdbc.sqlbuilder;


import com.duowan.common.jdbc.SqlBuilderType;
import com.duowan.common.utils.AssertUtil;

/**
 * 查询语句构建基类
 *
 * @author Arvin
 * @since 2018/05/22 13:58
 */
public abstract class AbstractQueryBuilder<T> extends AbstractSqlBuilder<T> implements SqlBuilder {

    /**
     * 最终的SQL
     */
    private volatile String sql = null;

    public AbstractQueryBuilder(Class<?> modelType, SqlBuilderType sqlBuilderType) {
        super(modelType, sqlBuilderType);
    }

    @Override
    public String getSql() {
        if (this.sql == null) {
            this.sql = buildSql();
        }
        return this.sql;
    }

    @Override
    protected void reset() {
        super.reset();
        this.sql = null;
    }

    public String buildSql() {
        reset();
        this.sql = build();
        AssertUtil.assertNotBlank(this.sql, "构建的SQL为空");
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * 构建SQL
     *
     * @return 返回一个sql
     */
    protected abstract String build();

    protected void logUsedSql(String sql) {
        if (logger.isDebugEnabled()) {
            String separator = "\n====================================================================================================================================================================";
            StringBuilder debugInfo = new StringBuilder(separator);
            debugInfo.append("\n" + this.getSqlBuilderType() + ": \t").append(sql);
            debugInfo.append("\nPARAMS: \t").append("COUNT = " + getParamsCount()).append("\t VALUES: ").append(paramsToString());
            debugInfo.append(separator);
            logger.debug(debugInfo.toString());
        }
    }
}
