package com.duowan.common.jdbc.sqlbuilder;

import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.MDContext;
import com.duowan.common.jdbc.OrderByQuery;
import com.duowan.common.jdbc.definition.ConditionItem;
import com.duowan.common.jdbc.definition.OrderItem;
import com.duowan.common.jdbc.definition.QueryCondDef;
import com.duowan.common.jdbc.definition.QueryCondFieldDef;
import com.duowan.common.jdbc.dialect.Dialect;
import com.duowan.common.jdbc.dialect.MysqlDialect;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.CommonUtil;

import java.util.*;

/**
 * 对象查询语句构建器
 *
 * @author Arvin
 */
public class MysqlSelectBuilder extends AbstractSelectBuilder<MysqlSelectBuilder> {

    private static final Dialect DIALECT = new MysqlDialect();

    /**
     * 查询条件对象, 可以为空，为空的话就是查询所有了
     */
    private Object queryCondition;

    /**
     * 查询条件定义
     */
    private QueryCondDef queryCondDef;

    /**
     * 查询条件类型
     */
    private Class<?> queryConditionType;

    /**
     * 查询条件的列和模型对象的列必须匹配
     */
    private boolean conditionAndModelColumnMustMatch = true;

    /**
     * @param modelType      要查询的类型
     * @param queryCondition 查询条件封装，通常是实体+Query 后缀对象
     */
    public MysqlSelectBuilder(Class<?> modelType, Object queryCondition) {
        super(modelType);
        this.queryCondition = queryCondition;

        this.queryConditionType = queryCondition != null ? queryCondition.getClass() : null;

        this.queryCondDef = this.queryConditionType == null ? null : MDContext.getQueryCondDef(queryConditionType);
    }

    public boolean isConditionAndModelColumnMustMatch() {
        return conditionAndModelColumnMustMatch;
    }

    public MysqlSelectBuilder setConditionAndModelColumnMustMatch(boolean conditionAndModelColumnMustMatch) {
        this.conditionAndModelColumnMustMatch = conditionAndModelColumnMustMatch;
        return this;
    }

    /**
     * 解析需要查询的条件查询项目
     */
    @Override
    public List<ConditionItem> parseConditionItemList() {

        List<ConditionItem> conditionItemList = new ArrayList<>();

        if (null == queryCondition) {
            return conditionItemList;
        }

        List<QueryCondFieldDef> queryConditionColumnDefinitionList = getQueryConditionColumnDefinitionList();

        AssertUtil.assertNotEmpty(queryConditionColumnDefinitionList, "不合法的查询条件对象，没有查询条件列！");

        for (QueryCondFieldDef queryConditionColumnDefinition : queryConditionColumnDefinitionList) {

            FieldDef modelColumnDefinition = getModelColumnDefinitionByQueryConditionColumnDefinition(queryConditionColumnDefinition);

            if (null != modelColumnDefinition && !isCustomIgnoreCondition(modelColumnDefinition)) {

                // 计算查询条件的值
                ConditionItem conditionItem = queryConditionColumnDefinition.createConditionItem(modelColumnDefinition);

                if (null != conditionItem) {
                    conditionItemList.add(conditionItem);
                }
            }
        }

        return conditionItemList;
    }

    /**
     * 根据查询条件列的定义查询指定的模型列定义
     *
     * @param queryConditionColumnDefinition 查询条件列定义
     * @return
     */
    private FieldDef getModelColumnDefinitionByQueryConditionColumnDefinition(QueryCondFieldDef queryConditionColumnDefinition) {

        AssertUtil.assertNotNull(queryConditionColumnDefinition, "查询条件列定义不能为null");

        String queryFieldName = queryConditionColumnDefinition.getQueryFieldName();

        try {
            return getFieldDefByModelFieldName(queryFieldName);
        } catch (Exception e) {
            AssertUtil.assertFalse(conditionAndModelColumnMustMatch,
                    "查询条件查询列[" + this.queryConditionType.getName() + "." + queryFieldName + "] 对应的模型[" + this.getModelType().getName() + "." + queryFieldName + "]不存在！");
        }
        return null;
    }

    /**
     * 获取查询列
     */
    private List<QueryCondFieldDef> getQueryConditionColumnDefinitionList() {
        if (null == this.queryCondition || null == this.queryCondDef) {
            return new ArrayList<>(0);
        }

        return this.queryCondDef.getQueryCondFieldDefs();
    }

    /**
     * 解析排序字段列表
     *
     * @return 返回需要排序的字段列表
     */
    @Override
    protected List<OrderItem> parseOrderItemList() {

        Set<OrderItem> orderItemSet = new HashSet<>();

        if (null != this.queryCondition) {

            // 计算这个查询条件类定义的
            appendQueryConditionFixedOrderItemList(orderItemSet);

            // 计算这个查询条件对象本身的
            appendQueryConditionOrderItemList(orderItemSet);
        }

        List<OrderItem> orderItemList = new ArrayList<>(orderItemSet);

        Collections.sort(orderItemList);

        return orderItemList;
    }

    private void appendQueryConditionOrderItemList(Set<OrderItem> orderItemSet) {
        if (this.queryCondition instanceof OrderByQuery) {
            OrderByQuery orderByQuery = (OrderByQuery) this.queryCondition;
            Set<OrderItem> tempOrderItemSet = orderByQuery.getOrderByItems();
            if (CommonUtil.isNotEmpty(tempOrderItemSet)) {
                orderItemSet.addAll(tempOrderItemSet);
            }
        }
    }

    private void appendQueryConditionFixedOrderItemList(Set<OrderItem> orderItemSet) {
        Set<OrderItem> queryConditionFixedOrderItemSet = getQueryConditionFixedOrderItemSet();
        if (CommonUtil.isNotEmpty(queryConditionFixedOrderItemSet)) {
            orderItemSet.addAll(queryConditionFixedOrderItemSet);
        }
    }

    private Set<OrderItem> getQueryConditionFixedOrderItemSet() {
        if (null != this.queryCondDef) {
            return this.queryCondDef.getFixedOrderItemSet(this.getModelType());
        }
        return new HashSet<>();
    }

    @Override
    protected WhereBuilder createWhereBuilder(Class<?> modelType) {
        return new MysqlWhereBuilder(modelType);
    }

    @Override
    protected Object getQueryCondition() {
        return this.queryCondition;
    }

    @Override
    protected MysqlSelectBuilder self() {
        return this;
    }

    @Override
    protected String wrapTableName(String tableName) {
        return DIALECT.wrapTableName(tableName);
    }

    @Override
    protected String wrapColumnName(String columnName) {
        return DIALECT.wrapColumnName(columnName);
    }
}
