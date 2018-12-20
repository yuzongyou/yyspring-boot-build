package com.duowan.common.jdbc.sqlbuilder;

import com.duowan.common.jdbc.enums.CompareType;
import com.duowan.common.jdbc.exception.JdbcException;
import com.duowan.common.utils.ReflectUtil;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/20 14:14
 */
public class WhereBuilderHelper {

    private WhereBuilderHelper() {
        throw new IllegalStateException("Helper class");
    }

    interface CompareConditionAppender {

        boolean append(WhereBuilder whereBuilder, Object queryConditionObject, String unWrapColumnName, Object value);
    }

    private static final CompareConditionAppender EQUAL_APPENDER = (whereBuilder, queryCondition, unWrapColumnName, value) -> {
        if (null == queryCondition || null == value) {
            return false;
        }
        whereBuilder.andEqual(unWrapColumnName, value);
        return true;
    };

    private static final CompareConditionAppender NOT_EQUAL_APPENDER = (whereBuilder, queryCondition, unWrapColumnName, value) -> {
        if (null == queryCondition || null == value) {
            return false;
        }
        whereBuilder.andNotEqual(unWrapColumnName, value);
        return true;
    };

    private static final CompareConditionAppender LIKE_APPENDER = (whereBuilder, queryCondition, unWrapColumnName, value) -> {
        if (null == queryCondition || null == value) {
            return false;
        }
        whereBuilder.andLike(unWrapColumnName, String.valueOf(value));
        return true;
    };

    private static final CompareConditionAppender IS_NULL_APPENDER = (whereBuilder, queryCondition, unWrapColumnName, value) -> {
        whereBuilder.andIsNull(unWrapColumnName);
        return true;
    };

    private static final CompareConditionAppender IS_NOT_NULL_APPENDER = (whereBuilder, queryCondition, unWrapColumnName, value) -> {
        whereBuilder.andIsNotNull(unWrapColumnName);
        return true;
    };

    private static final CompareConditionAppender LT_APPENDER = (whereBuilder, queryCondition, unWrapColumnName, value) -> {
        if (null == queryCondition || null == value) {
            return false;
        }
        whereBuilder.andLessThan(unWrapColumnName, value, false);
        return true;
    };

    private static final CompareConditionAppender LTANDEQ_APPENDER = (whereBuilder, queryCondition, unWrapColumnName, value) -> {
        if (null == queryCondition || null == value) {
            return false;
        }
        whereBuilder.andLessThan(unWrapColumnName, value, true);
        return true;
    };

    private static final CompareConditionAppender GT_APPENDER = (whereBuilder, queryCondition, unWrapColumnName, value) -> {
        if (null == queryCondition || null == value) {
            return false;
        }
        whereBuilder.andGreaterThan(unWrapColumnName, value, false);
        return true;
    };

    private static final CompareConditionAppender GTANDEQ_APPENDER = (whereBuilder, queryCondition, unWrapColumnName, value) -> {
        if (null == queryCondition || null == value) {
            return false;
        }
        whereBuilder.andGreaterThan(unWrapColumnName, value, true);
        return true;
    };

    private static Map<CompareType, CompareConditionAppender> appenderMap = loadCompareConditionAppenderMap();

    private static Map<CompareType, CompareConditionAppender> loadCompareConditionAppenderMap() {
        Map<CompareType, CompareConditionAppender> map = new EnumMap<>(CompareType.class);
        map.put(CompareType.EQUAL, EQUAL_APPENDER);
        map.put(CompareType.NOT_EQUAL, NOT_EQUAL_APPENDER);
        map.put(CompareType.LIKE, LIKE_APPENDER);
        map.put(CompareType.IS_NULL, IS_NULL_APPENDER);
        map.put(CompareType.IS_NOT_NULL, IS_NOT_NULL_APPENDER);
        map.put(CompareType.LT, LT_APPENDER);
        map.put(CompareType.LTANDEQ, LTANDEQ_APPENDER);
        map.put(CompareType.GT, GT_APPENDER);
        map.put(CompareType.GTANDEQ, GTANDEQ_APPENDER);

        return map;
    }

    public static boolean applyCompareCondition(WhereBuilder whereBuilder, Object queryConditionObject, Field queryConditionField, CompareType compareType, String unWrapColumnName) {

        Object value = null != queryConditionObject ? ReflectUtil.getFieldValue(queryConditionObject, queryConditionField) : null;
        CompareConditionAppender appender = appenderMap.get(compareType);

        if (null == appender) {
            throw new JdbcException("不支持的比较类型！");
        }

        return appender.append(whereBuilder, queryConditionObject, unWrapColumnName, value);
    }
}
