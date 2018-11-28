package com.duowan.common.jdbc.sqlbuilder;

import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.JdbcHelper;
import com.duowan.common.jdbc.SqlBuilderType;
import com.duowan.common.jdbc.dialect.MysqlDialect;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.ReflectUtil;

import java.util.List;

/**
 * 插入语句
 *
 * @author Arvin
 * @since 2018/05/22 9:22
 */
public class MysqlInsertBuilder extends AbstractSqlBuilder<MysqlInsertBuilder> implements SqlBuilder {

    /**
     * 要插入的模型對象
     */
    private Object model;

    /**
     * 填充自增ID
     */
    private boolean fillAutoIncrementKey = false;

    /**
     * 插入语句
     */
    private String insertSql;

    private MysqlDialect dialect = new MysqlDialect();

    public MysqlInsertBuilder(Object model) {
        super(model.getClass(), SqlBuilderType.INSERT);

        this.model = model;
    }

    @Override
    protected MysqlInsertBuilder self() {
        return this;
    }

    @Override
    protected String wrapTableName(String tableName) {
        return dialect.wrapTableName(tableName);
    }

    @Override
    protected String wrapColumnName(String columnName) {
        return dialect.wrapColumnName(columnName);
    }

    public boolean isFillAutoIncrementKey() {
        return fillAutoIncrementKey;
    }

    public void setFillAutoIncrementKey(boolean fillAutoIncrementKey) {
        this.fillAutoIncrementKey = fillAutoIncrementKey;
    }

    public FieldDef getAutoIncrementFd() {
        return this.getMd().getAutoIncrementFieldDef();
    }

    @Override
    public String getSql() {
        if (null != insertSql) {
            return insertSql;
        }
        insertSql = buildInsertSql();
        return insertSql;
    }

    /**
     * 构造 insert 语句
     *
     * @return 返回 INSERT SQL
     */
    private String buildInsertSql() {

        StringBuilder builder = new StringBuilder(getWrapTableName());
        StringBuilder fieldsBuilder = new StringBuilder("(");
        StringBuilder valuesBuilder = new StringBuilder(" VALUES(");

        List<FieldDef> fdList = getFdList();

        for (FieldDef fd : fdList) {
            if (isIgnoreInsertField(fd)) {
                continue;
            }
            if (fd.isPrimaryKey()) {
                appendPrimaryKeyColumn(fieldsBuilder, valuesBuilder, fd);
            } else if (fd.isUniqueKey()) {
                appendUniqueKeyColumn(fieldsBuilder, valuesBuilder, fd);
            } else {
                appendNormalColumn(fieldsBuilder, valuesBuilder, fd);
            }
        }
        fieldsBuilder.setLength(fieldsBuilder.length() - 1);
        fieldsBuilder.append(")");

        valuesBuilder.setLength(valuesBuilder.length() - 1);
        valuesBuilder.append(")");

        builder.append(fieldsBuilder.toString()).append(valuesBuilder.toString());

        return "INSERT INTO " + builder.toString();
    }

    private boolean isIgnoreInsertField(FieldDef fd) {
        if (fd.isInsertIgnore() || this.isIgnoreModelField(fd.getFieldName())) {
            return true;
        }
        if (fd.isPrimaryKey() && fd.isAutoIncrement()) {
            return true;
        }
        return false;
    }

    private void appendNormalColumn(StringBuilder fieldsBuilder, StringBuilder valuesBuilder, FieldDef fd) {
        if (!fd.isInsertIgnore()) {
            // 不忽略的情况下进行处理
            Object columnValue = ReflectUtil.getFieldValue(model, fd.getField());

            appendColumn(fieldsBuilder, valuesBuilder, fd, columnValue);
        }
    }

    private void appendUniqueKeyColumn(StringBuilder fieldsBuilder, StringBuilder valuesBuilder, FieldDef fd) {
        appendNormalColumn(fieldsBuilder, valuesBuilder, fd);
    }

    /**
     * 添加主键
     *
     * @param fieldsBuilder 属性构建器
     * @param valuesBuilder 值构建器
     * @param fd            列定义
     */
    private void appendPrimaryKeyColumn(StringBuilder fieldsBuilder, StringBuilder valuesBuilder, FieldDef fd) {
        Object pkValue = ReflectUtil.getFieldValue(model, fd.getField());

        if (fd.isAutoIncrement()) {
            // 自增的，检查一下是否有大于0， 大于0 的话就需要添加到语句中， 否则直接忽略
            if (JdbcHelper.isNumberPkValueOverZero(pkValue)) {
                appendColumn(fieldsBuilder, valuesBuilder, fd, pkValue);
            }
            return;
        } else if (fd.isUseUuid()) {
            if (!JdbcHelper.isValidUuid(pkValue)) {
                pkValue = JdbcHelper.generateUUID();
                ReflectUtil.setFieldValue(model, fd.getField(), pkValue);
                appendColumn(fieldsBuilder, valuesBuilder, fd, pkValue);
            } else {
                appendColumn(fieldsBuilder, valuesBuilder, fd, pkValue);
            }
            return ;
        }

        AssertUtil.assertNotNull(pkValue, "主键值不能为空！");

        appendColumn(fieldsBuilder, valuesBuilder, fd, pkValue);
    }

    private void appendColumn(StringBuilder fieldsBuilder, StringBuilder valuesBuilder, FieldDef fd, Object columnValue) {

        if (null == columnValue) {
            if (fd.isInsertIgnoreNull()) {
                return;
            }
        }

        if (null == columnValue) {
            valuesBuilder.append("null,");
        } else {
            valuesBuilder.append("?,");
            if (fd.getFieldType().isEnum()) {
                columnValue = String.valueOf(columnValue);
            }
            addParamByColumnDefinition(fd, columnValue);
        }

        fieldsBuilder.append(dialect.wrapColumnName(fd.getColumnName())).append(",");
    }
}
