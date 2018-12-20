package com.duowan.common.jdbc.definition;


import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.enums.CompareType;
import com.duowan.common.jdbc.exception.JdbcException;
import com.duowan.common.jdbc.sqlbuilder.WhereBuilder;
import com.duowan.common.utils.ReflectUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Arvin
 */
public class RangeConditionItem extends AbstractConditionItem<RangeCondFieldDef> {

    private static Set<CompareType> supportCompareTypeSet = new HashSet<>(Arrays.asList(
            CompareType.BETWEEN, CompareType.IS_NOT_NULL, CompareType.IS_NULL
    ));

    public RangeConditionItem(FieldDef modelColumnDefinition, RangeCondFieldDef queryConditionColumnDefinition) {
        super(modelColumnDefinition, queryConditionColumnDefinition);
    }

    @Override
    protected boolean isSupportCompareType(CompareType compareType) {
        return supportCompareTypeSet.contains(compareType);
    }

    @Override
    protected boolean customAppendToWhereBuilder(Object queryCondition, WhereBuilder whereBuilder, CompareType compareType) {

        String unWrapColumnName = getModelFieldDef().getColumnName();

        switch (compareType) {
            case IS_NULL:
                whereBuilder.andIsNull(unWrapColumnName);
                break;
            case IS_NOT_NULL:
                whereBuilder.andIsNotNull(unWrapColumnName);
                break;
            case BETWEEN:
                Field begField = getQueryCondFieldDef().getBegField();
                Field endField = getQueryCondFieldDef().getEndField();

                Object begValue = null != queryCondition ? ReflectUtil.getFieldValue(queryCondition, begField) : null;
                Object endValue = null != queryCondition ? ReflectUtil.getFieldValue(queryCondition, endField) : null;
                whereBuilder.andBetween(unWrapColumnName, begValue, endValue);
                break;
            default:
                throw new JdbcException("不支持的比较类型！");
        }

        return true;
    }

    @Override
    protected String getFieldStringInfo() {
        return "QueryCondition[" + this.getQueryCondFieldDef().getBegField().getName() + ", " + this.getQueryCondFieldDef().getEndField().getName() + "]";
    }
}
