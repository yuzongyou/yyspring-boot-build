package com.duowan.common.jdbc.definition;

import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.enums.CompareType;

/**
 * @author Arvin
 * @since 2017/11/17 13:41
 */
public interface QueryCondFieldDef {

    /**
     * 要查询的属性名称
     *
     * @return 查询属性名称
     */
    String getQueryFieldName();

    /**
     * 该字段查询的时候使用的查询类型
     *
     * @return 比较类型
     */
    CompareType getCompareType();

    /**
     * 创建查询条件项目
     *
     * @param fieldDef 模型对应的查询列定义
     * @return 条件项
     */
    ConditionItem createConditionItem(FieldDef fieldDef);

    /**
     * 根据模型类型创建排序项
     *
     * @param modelType 模型类型
     * @return 排序项
     */
    OrderItem createOrderItem(Class<?> modelType);
}
