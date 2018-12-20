package com.duowan.common.jdbc.definition;

import com.duowan.common.jdbc.annotations.IgnoreField;
import com.duowan.common.jdbc.annotations.NotRange;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.ReflectUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 查询条件定义
 *
 * @author Arvin
 */
public class QueryCondDef {

    /**
     * 记录范围查询的开始属性名称
     */
    public static final String BEG = "beg";
    public static final String END = "end";

    public static final String REGEX_BEG_END = "^(" + BEG + ")|(" + END + ")";

    public static final String SERIAL_VERSION_UID = "serialVersionUID";

    /**
     * 查询条件类型
     */
    private final Class<?> queryConditionType;

    /**
     * 查询列对象定义列表
     */
    private List<QueryCondFieldDef> queryCondFieldDefs = new ArrayList<>();

    private final List<Field> fieldList;

    public QueryCondDef(Class<?> queryConditionType) {
        this.queryConditionType = queryConditionType;

        this.fieldList = ReflectUtil.getAllNoneStaticDeclaredFields(this.queryConditionType);

        AssertUtil.assertNotEmpty(fieldList, "查询条件对象类[" + this.queryConditionType.getName() + "]没有查询属性");

        parseDefinition();

    }

    private void parseDefinition() {

        // 记录已经处理过了的属性名称集合
        Set<Field> processedFieldSet = new HashSet<>();

        for (Field field : fieldList) {
            if (!isIgnoreModelField(field) && !parseAsRangeQueryColumnDefinition(processedFieldSet, field)) {
                parseAsDirectQueryColumnDefinition(processedFieldSet, field);
            }
        }
    }

    /**
     * 当作范围查询字段来解析， 如果解析成功了就返回true， 否则返回false
     *
     * @param processedFieldSet 已经解析的字段集合
     * @param field             要解析的属性
     */
    private boolean parseAsRangeQueryColumnDefinition(Set<Field> processedFieldSet, Field field) {

        if (processedFieldSet.contains(field)) {
            return true;
        }

        NotRange notRangeAnnotation = ReflectUtil.getFieldAnnotation(field, NotRange.class);
        if (null != notRangeAnnotation) {
            return false;
        }

        String fieldName = field.getName();

        boolean isBeg = fieldName.startsWith(BEG);
        boolean isEnd = fieldName.startsWith(END);

        if (isBeg || isEnd) {

            String queryFieldNameWithFirstLetterUpperCase = fieldName.replaceFirst(REGEX_BEG_END, "");

            String queryFieldName = CommonUtil.firstLetterToLowerCase(queryFieldNameWithFirstLetterUpperCase);

            // 相反的属性名称
            String oppositeFieldName = isBeg ? END + queryFieldNameWithFirstLetterUpperCase : BEG + queryFieldNameWithFirstLetterUpperCase;

            Field oppositeField = lookupQueryFieldByName(oppositeFieldName);

            // 说明有相反的属性
            if (null != oppositeField) {

                Field begField = isBeg ? field : oppositeField;
                Field endField = isEnd ? field : oppositeField;

                RangeCondFieldDef fieldDef =
                        new RangeCondFieldDef(begField, endField, queryFieldName, this);

                this.queryCondFieldDefs.add(fieldDef);

                processedFieldSet.add(begField);
                processedFieldSet.add(endField);

                return true;
            }
        }

        return false;
    }

    private Field lookupQueryFieldByName(String fieldName) {
        for (Field field : this.fieldList) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }


    /**
     * 作为直接进行查询的属性解析， 如果成功返回true，否则返回false
     *
     * @param processedFieldSet 已经处理的属性集合
     * @param field             要解析的属性
     */
    private boolean parseAsDirectQueryColumnDefinition(Set<Field> processedFieldSet, Field field) {

        if (!processedFieldSet.contains(field)) {

            DirectCondFieldDef columnDefinition = new DirectCondFieldDef(field, this);

            processedFieldSet.add(field);

            this.queryCondFieldDefs.add(columnDefinition);
        }
        return true;
    }

    /**
     * 忽略不必要的属性
     *
     * @param modelField 要检查的属性
     * @return
     */
    private boolean isIgnoreModelField(Field modelField) {
        if (SERIAL_VERSION_UID.equalsIgnoreCase(modelField.getName())) {
            return true;
        }

        IgnoreField ignoreFieldAnnotation = ReflectUtil.getFieldAnnotation(modelField, IgnoreField.class);

        return null != ignoreFieldAnnotation;

    }


    public List<QueryCondFieldDef> getQueryCondFieldDefs() {
        return queryCondFieldDefs;
    }

    /**
     * 缓存模型到 排序项目的一个集合
     */
    private Map<Class<?>, Set<OrderItem>> modelTypeToFixedOrderItemSetMap = new HashMap<>();

    public Set<OrderItem> getFixedOrderItemSet(Class<?> modelType) {

        AssertUtil.assertNotNull(modelType, "要获取排序项的模型类型不能为空！");

        Set<OrderItem> orderItemSet = modelTypeToFixedOrderItemSetMap.get(modelType);

        if (null != orderItemSet) {
            return orderItemSet;
        }

        orderItemSet = new HashSet<>();

        for (QueryCondFieldDef fieldDef : this.queryCondFieldDefs) {
            OrderItem orderItem = fieldDef.createOrderItem(modelType);
            if (null != orderItem) {
                orderItemSet.add(orderItem);
            }
        }

        orderItemSet = Collections.unmodifiableSet(orderItemSet);

        modelTypeToFixedOrderItemSetMap.put(modelType, orderItemSet);

        return orderItemSet;
    }

    public Class<?> getQueryConditionType() {
        return queryConditionType;
    }
}
