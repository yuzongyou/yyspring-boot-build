package com.duowan.common.jdbc.definition;

import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.JdbcHelper;
import com.duowan.common.jdbc.enums.CompareType;
import com.duowan.common.jdbc.exception.SqlBuilderException;
import com.duowan.common.jdbc.sqlbuilder.WhereBuilder;
import com.duowan.common.utils.ReflectUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Arvin
 */
public class DirectConditionItem extends AbstractConditionItem<DirectCondFieldDef> {

    private static Set<CompareType> supportCompareTypeSet = new HashSet<CompareType>(Arrays.asList(
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
        if (JdbcHelper.isStringType(fieldType) && CompareType.LIKE.equals(compareType)) {
            return true;
        }

        return false;
    }

    @Override
    protected boolean customAppendToWhereBuilder(Object queryCondition, WhereBuilder whereBuilder, CompareType compareType) {

        String unWrapColumnName = getModelFieldDef().getColumnName();

        Field queryField = getQueryCondFieldDef().getField();

        Object value = null != queryCondition ? ReflectUtil.getFieldValue(queryCondition, queryField) : null;

        switch (compareType) {
            case EQUAL:
                if (null == queryCondition || null == value) {
                    return false;
                }
                whereBuilder.andEqual(unWrapColumnName, value);
                break;
            case NOT_EQUAL:
                if (null == queryCondition || null == value) {
                    return false;
                }
                whereBuilder.andNotEqual(unWrapColumnName, value);
                break;
            case LIKE:
                if (null == queryCondition || null == value) {
                    return false;
                }
                whereBuilder.andLike(unWrapColumnName, String.valueOf(value));
                break;
            case IS_NULL:
                whereBuilder.andIsNull(unWrapColumnName);
                break;
            case IS_NOT_NULL:
                whereBuilder.andIsNotNull(unWrapColumnName);
                break;
            case LT:
                if (null == queryCondition || null == value) {
                    return false;
                }
                whereBuilder.andLessThan(unWrapColumnName, value, false);
                break;
            case LTANDEQ:
                if (null == queryCondition || null == value) {
                    return false;
                }
                whereBuilder.andLessThan(unWrapColumnName, value, true);
                break;
            case GT:
                if (null == queryCondition || null == value) {
                    return false;
                }
                whereBuilder.andGreaterThan(unWrapColumnName, value, false);
                break;
            case GTANDEQ:
                if (null == queryCondition || null == value) {
                    return false;
                }
                whereBuilder.andGreaterThan(unWrapColumnName, value, true);
                break;
            default:
                throw new SqlBuilderException("不支持的比较类型！");
        }

        return true;
    }

    @Override
    protected String getFieldStringInfo() {
        return "QueryCondition[" + this.getModelFieldDef().getField().getName() + "]";
    }
}
