package com.duowan.common.jdbc.sqlbuilder;


import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.SqlBuilderType;
import com.duowan.common.utils.ReflectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arvin
 */
public class MysqlUpdateByPrimaryKeyBuilder extends AbstractMysqlUpdateBuilder<MysqlUpdateByPrimaryKeyBuilder> {

    public MysqlUpdateByPrimaryKeyBuilder(Object model) {
        super(model, SqlBuilderType.UPDATE);
    }

    @Override
    public String getSql() {
        String sql = super.getSql();
        logUsedSql(sql);
        return sql;
    }

    @Override
    protected String build() {

        reset();

        StringBuilder sqlBuilder = new StringBuilder("UPDATE ").append(getWrapTableName()).append(" SET ");

        List<FieldDef> needUpdateColumnDefinitions = calculateNeedUpdateColumnDefinitions();

        // 添加要更新的数据库列
        appendUpdateColumns(sqlBuilder, needUpdateColumnDefinitions);

        boolean isPrimaryKeyAppendToWhere = lookupAndAppendPrimaryKeyToWhereClause(sqlBuilder);

        if (isPrimaryKeyAppendToWhere) {
            sqlBuilder.append("LIMIT 1");
            return sqlBuilder.toString();
        }

        throw new IllegalArgumentException("根据主键构造更新语句失败, 主键值未设置！ \nsql=" + sqlBuilder.toString());

    }

    /**
     * <pre>
     * 计算需要更新的字段属性列表, 计算规则为：
     *
     * 1. 如果用户自定义了，直接使用自定义的
     * 2. 用户没有自定义，所有值不允许为空的， 但是model中为空的将会被忽略
     * </pre>
     */
    private List<FieldDef> calculateNeedUpdateColumnDefinitions() {

        List<FieldDef> tempList = new ArrayList<>();

        tempList.addAll(getMd().getAllDefList());

        // 清理掉不允许NULL但是目前的值是NULL的列
        List<FieldDef> resultList = new ArrayList<>();

        for (FieldDef fd : tempList) {

            if (fd.isUpdateIgnore() || fd.isPrimaryKey() || isIgnoreModelField(fd.getFieldName())) {
                continue;
            }

            Object value = ReflectUtil.getFieldValue(this.model, fd.getField());
            if (null != value) {
                resultList.add(fd);
            } else {
                if (!fd.isUpdateIgnoreNull()) {
                    resultList.add(fd);
                }
            }
        }

        return resultList;
    }

    @Override
    protected MysqlUpdateByPrimaryKeyBuilder self() {
        return this;
    }

}
