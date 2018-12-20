package com.duowan.common.jdbc.definition;

import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.JdbcHelper;
import com.duowan.common.jdbc.enums.CompareType;
import com.duowan.common.jdbc.sqlbuilder.WhereBuilder;
import com.duowan.common.jdbc.sqlbuilder.WhereBuilderHelper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Arvin
 */
public class DirectConditionItem extends AbstractConditionItem<DirectCondFieldDef> {

    private static Set<CompareType> supportCompareTypeSet = new HashSet<>(Arrays.asList(
            CompareType.EQUAL, CompareType.IS_NOT_NULL, CompareType.IS_NULL, CompareType.NOT_EQUAL, CompareType.GT, CompareType.GTANDEQ, CompareType.LT, CompareType.LTANDEQ
    ));

    public DirectConditionItem(FieldDef fieldDef, DirectCondFieldDef queryCondFieldDef) {
        super(fieldDef, queryCondFieldDef);
    }

    @Override
    protected boolean isSupportCompareType(CompareType compareType) {

        if (supportCompareTypeSet.contains(compareType)) {
            return true;
        }

        // 字符串类型才支持 LIKE 查询
        Class<?> fieldType = getQueryCondFieldDef().getFieldType();
        return JdbcHelper.isStringType(fieldType) && CompareType.LIKE.equals(compareType);

    }

    @Override
    protected boolean customAppendToWhereBuilder(Object queryCondition, WhereBuilder whereBuilder, CompareType compareType) {

        String unWrapColumnName = getModelFieldDef().getColumnName();

        Field queryField = getQueryCondFieldDef().getField();

        return WhereBuilderHelper.applyCompareCondition(whereBuilder, queryCondition, queryField, compareType, unWrapColumnName);
    }

    @Override
    protected String getFieldStringInfo() {
        return "QueryCondition[" + this.getModelFieldDef().getField().getName() + "]";
    }
}
