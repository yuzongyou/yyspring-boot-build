package com.duowan.common.jdbc.sqlbuilder;

import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.JdbcHelper;
import com.duowan.common.jdbc.SqlBuilderType;
import com.duowan.common.jdbc.dialect.Dialect;
import com.duowan.common.jdbc.dialect.MysqlDialect;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.ReflectUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Arvin
 */
public class MysqlUpdateBuilder extends AbstractMysqlUpdateBuilder<MysqlUpdateBuilder> {

    protected static final Dialect DIALECT = new MysqlDialect();

    /**
     * Where 语句构造器
     */
    private MysqlWhereBuilder whereBuilder;

    /**
     * 更新的数目, -1 表示全部更新
     */
    private int updateLimitCount = -1;

    /**
     * 是否允许where子句为空
     */
    private boolean allowNullWhereCondition = false;

    /**
     * 自定义要更新的列名称集合， 如果没有设置的话就按照默认规则进行计算
     */
    private Set<FieldDef> customNeedUpdateColumnDefinitionSet = new HashSet<>();

    /**
     * @param model            要更新的对象
     * @param updateLimitCount 要更新的数量，如果要限制所有请传一个小于 0 的数字， 如 -1
     */
    public MysqlUpdateBuilder(Object model, int updateLimitCount) {
        super(model, SqlBuilderType.UPDATE);
        this.setUpdateLimitCount(updateLimitCount);
    }

    /**
     * @param whereBuilder     where 条件
     * @param updateLimitCount 要更新的数量，如果要限制所有请传一个小于 0 的数字， 如 -1
     * @param model            要更新的对象
     */
    public MysqlUpdateBuilder(MysqlWhereBuilder whereBuilder, int updateLimitCount, Object model) {
        super(model, SqlBuilderType.UPDATE);

        this.whereBuilder = whereBuilder;
        this.setUpdateLimitCount(updateLimitCount);
    }

    public MysqlWhereBuilder getWhereBuilder() {
        return whereBuilder;
    }

    public MysqlUpdateBuilder setWhereBuilder(MysqlWhereBuilder whereBuilder) {
        this.whereBuilder = whereBuilder;
        return this;
    }

    public MysqlUpdateBuilder setUpdateLimitCount(int updateLimitCount) {
        AssertUtil.assertTrue(updateLimitCount != 0, "要更新的数量不能为0");
        this.updateLimitCount = updateLimitCount < 0 ? -1 : updateLimitCount;
        return this;
    }

    public int getUpdateLimitCount() {
        return updateLimitCount;
    }

    public boolean isAllowNullWhereCondition() {
        return allowNullWhereCondition;
    }

    public MysqlUpdateBuilder setAllowNullWhereCondition(boolean allowNullWhereCondition) {
        this.allowNullWhereCondition = allowNullWhereCondition;
        return this;
    }

    @Override
    public String getSql() {
        String sql = super.getSql();
        logUsedSql(sql);
        return sql;
    }

    /**
     * <pre>
     * 规则：
     *  1. 如果调用的时候，指定了用于更新的主键，则使用自定义的
     *  2. 先寻找主键， 如果主键不为空就按照主键进行更新
     *  3. 寻找唯一能够标识一行数据的字段，即 UniqueKey 的字段，注意只能有一个
     * </pre>
     *
     * @return 返回sql
     */
    @Override
    protected String build() {

        reset();

        StringBuilder sqlBuilder = new StringBuilder("UPDATE ").append(getWrapTableName()).append(" SET ");

        List<FieldDef> needUpdateColumnDefinitions = calculateNeedUpdateColumnDefinitions();

        // 添加要更新的数据库列
        appendUpdateColumns(sqlBuilder, needUpdateColumnDefinitions);

        // 添加 WhereBuilder 构造的条件
        boolean isWhereBuilderAppend = lookupAndAppendWhereBuilderCondition(sqlBuilder);

        if (isWhereBuilderAppend) {
            lookupAndAppendUpdateLimitCount(sqlBuilder);
            return sqlBuilder.toString();
        }

        boolean isPrimaryKeyAppendToWhere = lookupAndAppendPrimaryKeyToWhereClause(sqlBuilder);

        if (isPrimaryKeyAppendToWhere) {
            lookupAndAppendUpdateLimitCount(sqlBuilder);
            return sqlBuilder.toString();
        }

        boolean isUniqueKeyAppendToWhere = lookupAndAppendUniqueKeyToWhereClause(sqlBuilder);

        if (isUniqueKeyAppendToWhere) {
            lookupAndAppendUpdateLimitCount(sqlBuilder);
            return sqlBuilder.toString();
        }

        AssertUtil.assertTrue(isAllowNullWhereCondition(),
                "你的更新语句没有设置where子句 太危险了，如果确实需要更新所有符合条件的请调用: UpdateBuilder.setAllowNullWhereCondition(true): \nsql=" + sqlBuilder.toString());

        lookupAndAppendUpdateLimitCount(sqlBuilder);

        return sqlBuilder.toString();
    }

    /**
     * <pre>
     * 计算需要更新的字段属性列表, 计算规则为：
     *
     * 1. 如果用户自定义了，直接使用自定义的
     * 2. 用户没有自定义，所有值不允许为空的， 但是model中为空的将会被忽略
     * </pre>
     *
     * @return 返回属性定义列表
     */
    private List<FieldDef> calculateNeedUpdateColumnDefinitions() {

        List<FieldDef> tempList = new ArrayList<>();
        fillUpdateColumnDefinitions(tempList);

        // 清理掉不允许NULL但是目前的值是NULL的列
        List<FieldDef> resultList = new ArrayList<>();

        for (FieldDef fd : tempList) {

            if (fd.isUpdateIgnore() || isIgnoreModelField(fd.getFieldName())) {
                continue;
            }

            Object value = ReflectUtil.getFieldValue(this.model, fd.getField());
            if (null != value) {
                addNeedUpdateColumnForNotNullValue(resultList, fd, value);
            } else {
                addNeedUpdateColumnForNullValue(resultList, fd);
            }
        }

        return resultList;
    }

    private void addNeedUpdateColumnForNullValue(List<FieldDef> resultList, FieldDef fd) {
        if (!fd.isUpdateIgnoreNull()) {
            resultList.add(fd);
        }
    }

    private void addNeedUpdateColumnForNotNullValue(List<FieldDef> resultList, FieldDef fd, Object value) {
        if (fd.isPrimaryKey()) {
            if (JdbcHelper.isValidPrimaryKeyValue(value)) {
                resultList.add(fd);
            }
        } else {
            resultList.add(fd);
        }
    }

    private void fillUpdateColumnDefinitions(List<FieldDef> resultList) {

        if (!this.customNeedUpdateColumnDefinitionSet.isEmpty()) {
            resultList.addAll(this.customNeedUpdateColumnDefinitionSet);
        } else {
            // 没有自定义，计算
            resultList.addAll(getMd().getAllDefList());
        }

    }

    private boolean lookupAndAppendUniqueKeyToWhereClause(StringBuilder sqlBuilder) {
        // 检查是否有唯一键的值不为空，如有有的话就根据唯一键进行更新
        List<FieldDef> ukfdList = getMd().getUniqueKeyDefList();
        if (null != ukfdList && !ukfdList.isEmpty()) {
            for (FieldDef fd : ukfdList) {
                Object value = ReflectUtil.getFieldValue(this.model, fd.getField());
                if (null != value && StringUtils.isNotBlank(String.valueOf(value))) {
                    sqlBuilder.append(" WHERE ").append(wrapColumnName(fd.getColumnName())).append("=?");
                    addParamByColumnDefinition(fd, value);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean lookupAndAppendUpdateLimitCount(StringBuilder sqlBuilder) {
        // 添加Limit
        if (updateLimitCount > 0) {
            sqlBuilder.append("LIMIT ").append(updateLimitCount);
            return true;
        }
        return false;
    }

    /**
     * 如果有设置WhereBuilder 就添加过去，并返回 true 表示添加了， 否则返回 false
     *
     * @param sqlBuilder sql 语句构造器
     * @return 是否添加成功
     */
    private boolean lookupAndAppendWhereBuilderCondition(StringBuilder sqlBuilder) {
        if (this.whereBuilder != null && this.whereBuilder.conditionCount() > 0) {
            sqlBuilder.append(" ").append(whereBuilder.getSql());
            addListSqlParams(whereBuilder.getSqlParamList());
            return true;
        }
        return false;
    }

    /**
     * 添加要进行更新的java属性名称
     *
     * @param needUpdateModelFieldNames 需要更新的属性数组
     * @return UpdateBuilder 本身
     */
    public MysqlUpdateBuilder addUpdateModelFieldNames(String... needUpdateModelFieldNames) {
        if (null != needUpdateModelFieldNames && needUpdateModelFieldNames.length > 0) {
            for (String modelFieldName : needUpdateModelFieldNames) {
                if (StringUtils.isNotBlank(modelFieldName)) {
                    FieldDef fd = getMd().getFieldDefByModelFieldName(modelFieldName);

                    if (null != fd) {
                        this.customNeedUpdateColumnDefinitionSet.add(fd);
                    }
                }
            }
        }
        return this;
    }

    public MysqlUpdateBuilder removeUpdateModelFieldNames(String... needUpdateModelFieldNames) {
        if (null != needUpdateModelFieldNames && needUpdateModelFieldNames.length > 0) {
            for (String modelFieldName : needUpdateModelFieldNames) {
                if (StringUtils.isNotBlank(modelFieldName)) {
                    FieldDef fd = getMd().getFieldDefByModelFieldName(modelFieldName);
                    if (null != fd) {
                        this.customNeedUpdateColumnDefinitionSet.remove(fd);
                    }
                }
            }
        }
        return this;
    }

    @Override
    protected MysqlUpdateBuilder self() {
        return this;
    }

    @Override
    protected String wrapTableName(String tableName) {
        return DIALECT.wrapTableName(tableName);
    }
}
