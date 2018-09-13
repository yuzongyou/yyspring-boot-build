package com.duowan.common.jdbc.definition;

import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.enums.CompareType;

import java.lang.reflect.Field;

/**
 * 范围查询
 *
 * @author Arvin
 * @since 2017/11/17 14:24
 */
public class RangeCondFieldDef extends AbstractQueryCondFieldDef {

    /**
     * 开始属性
     */
    private final Field begField;
    /**
     * 结束属性
     */
    private final Field endField;

    public RangeCondFieldDef(Field begField, Field endField, String queryFieldName, QueryCondDef queryConditionDefinition) {
        super(queryFieldName, CompareType.BETWEEN, queryConditionDefinition);

        this.begField = begField;
        this.endField = endField;

    }

    @Override
    protected OrderItem customCreateOrderItem(Class<?> modelType) {

        OrderItem orderItem = createOrderItemByField(modelType, begField);

        if (null == orderItem) {
            orderItem = createOrderItemByField(modelType, endField);
        }

        return orderItem;
    }

    @Override
    protected ConditionItem customCreateConditionItem(FieldDef modelColumnDefinition) {
        return new RangeConditionItem(modelColumnDefinition, this);
    }

    public Field getBegField() {
        return begField;
    }

    public Field getEndField() {
        return endField;
    }
}
