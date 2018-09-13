package com.duowan.common.jdbc.definition;

import com.duowan.common.jdbc.enums.CompareType;
import com.duowan.common.jdbc.sqlbuilder.WhereBuilder;

import java.lang.reflect.Field;

/**
 * @author Arvin
 */
public interface ConditionItem {

    /**
     * 返回是否进行了追加
     *
     * @param queryCondition 查询条件对象
     * @param whereBuilder   Where子句构造器
     * @param compareType    比较类型，如果为null的话使用默认的规则获取比较类型
     * @return 返回是否进行了追加
     */
    boolean appendToWhereBuilder(Object queryCondition, WhereBuilder whereBuilder, CompareType compareType);

    /**
     * 获取查询条件的字段
     *
     * @return 返回查询条件字段
     */
    Field getQueryConditionField();
}
