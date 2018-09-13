package com.duowan.common.jdbc.definition;

import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.annotations.QueryCompare;
import com.duowan.common.jdbc.enums.CompareType;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.ReflectUtil;

import java.lang.reflect.Field;

/**
 * 直接字段查询
 *
 * @author Arvin
 */
public class DirectCondFieldDef extends AbstractQueryCondFieldDef {

    /**
     * 解析比较类型
     *
     * @param field              属性
     * @param defaultCompareType 默认查询类型
     * @return
     */
    private static CompareType parseCompareType(Field field, CompareType defaultCompareType) {
        AssertUtil.assertNotNull(field, "要计算比较类型的属性不能为空！");
        defaultCompareType = null == defaultCompareType ? CompareType.EQUAL : defaultCompareType;

        QueryCompare queryCompareAnn = ReflectUtil.getFieldAnnotation(field, QueryCompare.class);
        if (null == queryCompareAnn || null == queryCompareAnn.value()) {
            return defaultCompareType;
        }
        return queryCompareAnn.value();
    }

    /**
     * 查询条件字段
     */
    private final Field field;

    public DirectCondFieldDef(Field field, QueryCondDef queryCondDef) {
        super(field.getName(), parseCompareType(field, CompareType.EQUAL), queryCondDef);

        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public Class<?> getFieldType() {
        return this.field.getType();
    }

    @Override
    protected OrderItem customCreateOrderItem(Class<?> modelType) {

        return createOrderItemByField(modelType, this.field);
    }

    @Override
    protected ConditionItem customCreateConditionItem(FieldDef fieldDef) {
        return new DirectConditionItem(fieldDef, this);
    }
}
