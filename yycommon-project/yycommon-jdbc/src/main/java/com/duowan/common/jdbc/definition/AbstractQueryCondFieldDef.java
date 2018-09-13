package com.duowan.common.jdbc.definition;

import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.annotations.OrderAsc;
import com.duowan.common.jdbc.annotations.OrderDesc;
import com.duowan.common.jdbc.enums.CompareType;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.ReflectUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arvin
 * @since 2017/11/17 14:27
 */
public abstract class AbstractQueryCondFieldDef implements QueryCondFieldDef {

    protected final String queryFieldName;

    protected final QueryCondDef queryConditionDefinition;

    protected final CompareType compareType;

    protected AbstractQueryCondFieldDef(String queryFieldName, CompareType compareType, QueryCondDef queryCondDef) {
        AssertUtil.assertNotBlank(queryFieldName, "要查询的Java字段名称不能为空！");
        AssertUtil.assertNotNull(queryCondDef, "查询条件对象定义不能为空！");
        AssertUtil.assertNotNull(compareType, "查询字段的比较类型不能为空！");

        this.queryFieldName = queryFieldName;
        this.queryConditionDefinition = queryCondDef;
        this.compareType = compareType;
    }

    @Override
    public String getQueryFieldName() {
        return queryFieldName;
    }

    @Override
    public CompareType getCompareType() {
        return compareType;
    }

    /**
     * 模型类型列定义 --> 查询项
     */
    private Map<FieldDef, ConditionItem> columnDefinitionConditionItemMap = new HashMap<FieldDef, ConditionItem>();

    @Override
    public ConditionItem createConditionItem(FieldDef modelColumnDefinition) {

        if (columnDefinitionConditionItemMap.containsKey(modelColumnDefinition)) {
            return columnDefinitionConditionItemMap.get(modelColumnDefinition);
        }

        ConditionItem conditionItem = customCreateConditionItem(modelColumnDefinition);

        columnDefinitionConditionItemMap.put(modelColumnDefinition, conditionItem);

        return conditionItem;
    }

    /**
     * 创建自定义的 查询条件项
     *
     * @param modelColumnDefinition 模型列定义
     * @return 返回条件字段
     */
    protected abstract ConditionItem customCreateConditionItem(FieldDef modelColumnDefinition);

    /**
     * 模型类型 -- 对应的排序项Map
     */
    private Map<Class<?>, OrderItem> modelTypeToOrderItemMap = new HashMap<Class<?>, OrderItem>();

    @Override
    public final OrderItem createOrderItem(Class<?> modelType) {

        AssertUtil.assertNotNull(modelType, "要创建排序项的模型类型不能为空！");

        if (modelTypeToOrderItemMap.containsKey(modelType)) {
            return modelTypeToOrderItemMap.get(modelType);
        }

        OrderItem orderItem = customCreateOrderItem(modelType);

        modelTypeToOrderItemMap.put(modelType, orderItem);

        return orderItem;
    }


    /**
     * 创建自定义的排序项目
     *
     * @param modelType 模型类型
     * @return 可以返回null如果不存在的话
     */
    protected abstract OrderItem customCreateOrderItem(Class<?> modelType);

    /**
     * 根据属性计算排序项
     *
     * @param modelType 模型类型
     * @param field     属性名称
     * @return 如果不存在则返回null
     */
    protected OrderItem createOrderItemByField(Class<?> modelType, Field field) {

        // 检查是否有排序相关属性： OrderAsc， OrderDesc
        OrderAsc orderAscAnnotation = ReflectUtil.getFieldAnnotation(field, OrderAsc.class);
        if (null != orderAscAnnotation) {
            return new OrderItem(orderAscAnnotation.order(), modelType, getQueryFieldName(), true);
        }

        OrderDesc orderDescAnnotation = ReflectUtil.getFieldAnnotation(field, OrderDesc.class);
        if (null != orderDescAnnotation) {
            return new OrderItem(orderDescAnnotation.order(), modelType, getQueryFieldName(), false);
        }

        return null;
    }
}
