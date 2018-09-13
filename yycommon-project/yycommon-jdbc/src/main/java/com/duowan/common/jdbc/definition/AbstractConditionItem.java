package com.duowan.common.jdbc.definition;


import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.enums.CompareType;
import com.duowan.common.jdbc.sqlbuilder.WhereBuilder;
import com.duowan.common.utils.AssertUtil;

import java.lang.reflect.Field;

/**
 * 查询条件项
 *
 * @author Arvin
 */
public abstract class AbstractConditionItem<T extends QueryCondFieldDef> implements ConditionItem {

    /**
     * 对应模型列定义
     */
    private final FieldDef fieldDef;

    /**
     * 对应查询条件列定义
     */
    private final T queryCondFieldDef;

    public AbstractConditionItem(FieldDef fieldDef, T queryCondFieldDef) {

        AssertUtil.assertNotNull(fieldDef, "模型列定义不能为空！");
        AssertUtil.assertNotNull(queryCondFieldDef, "查询条件列定义不能为空！");

        AssertUtil.assertTrue(fieldDef.getFieldName().equals(queryCondFieldDef.getQueryFieldName()),
                "模型列和查询条件列的名称应该一致");

        this.fieldDef = fieldDef;
        this.queryCondFieldDef = queryCondFieldDef;
    }

    public T getQueryCondFieldDef() {
        return queryCondFieldDef;
    }

    public FieldDef getModelFieldDef() {
        return fieldDef;
    }

    @Override
    public Field getQueryConditionField() {
        return getModelFieldDef().getField();
    }


    /**
     * 返回是否进行了追加
     *
     * @param queryCondition 查询条件
     * @param whereBuilder   Where子句构造器
     * @param compareType    比较类型
     * @return 返回是否进行了追加
     */
    @Override
    public boolean appendToWhereBuilder(Object queryCondition, WhereBuilder whereBuilder, CompareType compareType) {
        AssertUtil.assertNotNull(whereBuilder, "WhereBuilder 不能为空！");

        compareType = null == compareType ? getQueryCondFieldDef().getCompareType() : compareType;

        AssertUtil.assertTrue(null != compareType && isSupportCompareType(compareType), getFieldStringInfo() + " 不支持比较类型 [" + compareType + "]！");

        return customAppendToWhereBuilder(queryCondition, whereBuilder, compareType);
    }

    /**
     * 属性信息
     *
     * @return 返回字段信息
     */
    protected abstract String getFieldStringInfo();

    /**
     * 验证是否是支持的查询类型
     *
     * @param compareType 要验证的查询类型
     * @return 是否支持
     */
    protected abstract boolean isSupportCompareType(CompareType compareType);

    /**
     * 自定义 append 策略
     *
     * @param compareType    比较类型
     * @param queryCondition 查询条件
     * @param whereBuilder   where 语句构造器
     * @return 是否成功
     */
    protected abstract boolean customAppendToWhereBuilder(Object queryCondition, WhereBuilder whereBuilder, CompareType compareType);

}
