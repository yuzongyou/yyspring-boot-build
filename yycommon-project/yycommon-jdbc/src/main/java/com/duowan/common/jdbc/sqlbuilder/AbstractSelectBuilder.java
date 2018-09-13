package com.duowan.common.jdbc.sqlbuilder;

import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.JdbcHelper;
import com.duowan.common.jdbc.SqlBuilderType;
import com.duowan.common.jdbc.definition.ConditionItem;
import com.duowan.common.jdbc.definition.OrderItem;
import com.duowan.common.jdbc.enums.CompareType;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.CommonUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Arvin
 */
public abstract class AbstractSelectBuilder<T> extends AbstractQueryBuilder<T> {

    /**
     * 分页查询的时候，页码
     */
    private int pageNo;
    /**
     * 分页查询的时候，每页查询多少记录
     */
    private int pageSize;

    /**
     * Order By 子句 sql
     */
    private String orderBySql;

    /**
     * limit语句sql
     */
    private String limitSql;

    /**
     * 要查询的列语句
     */
    private String selectColumnSql;

    /**
     * Where 条件构造器
     */
    private final WhereBuilder whereBuilder;

    private WhereBuilder customWhereBuilder;

    /**
     * 子条件查询
     */
    private WhereBuilder subWhereBuilder;

    /**
     * 自定义查询列Sql
     */
    private String customSelectColumnSql;

    /**
     * 自定义要查询的列
     */
    private List<FieldDef> customSelectFieldDefs = new ArrayList<FieldDef>();

    /**
     * 自定义要忽略查询的列
     */
    private List<FieldDef> customIgnoreSelectFieldDefs = new ArrayList<FieldDef>();

    /**
     * 查询条件项
     */
    private List<ConditionItem> conditionItemList = new ArrayList<ConditionItem>();

    /**
     * 展开查询列
     */
    private boolean expandSelectColumn = false;

    public AbstractSelectBuilder(Class<?> modelType) {
        super(modelType, SqlBuilderType.SELECT);

        this.whereBuilder = createWhereBuilder(modelType);
    }

    protected abstract WhereBuilder createWhereBuilder(Class<?> modelType);

    public WhereBuilder getCustomWhereBuilder() {
        return customWhereBuilder;
    }

    public T setWhereBuilder(WhereBuilder customWhereBuilder) {
        this.customWhereBuilder = customWhereBuilder;
        return self();
    }

    public WhereBuilder getSubWhereBuilder() {
        return subWhereBuilder;
    }

    public void setSubWhereBuilder(WhereBuilder subWhereBuilder) {
        this.subWhereBuilder = subWhereBuilder;
    }

    public boolean isExpandSelectColumn() {
        return expandSelectColumn;
    }

    public void setExpandSelectColumn(boolean expandSelectColumn) {
        this.expandSelectColumn = expandSelectColumn;
    }

    /**
     * 设置分页参数
     *
     * @param pageNo   要查询的页码
     * @param pageSize 每页显示多少条
     * @return Sql构造器本身
     */
    public T limit(int pageNo, int pageSize) {
        AssertUtil.assertTrue(pageNo > 0 && pageSize > 0, "分页参数不正确，要求 pageNo > 0 并且 pageSize > 0");
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        return self();
    }

    /**
     * 只查询一个
     *
     * @return Sql构造器本身
     */
    public T limitOne() {
        return limit(1, 1);
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getCustomSelectColumnSql() {
        return customSelectColumnSql;
    }

    public void setCustomSelectColumnSql(String customSelectColumnSql) {
        this.customSelectColumnSql = customSelectColumnSql;
    }

    public T addCustomSelectModelFieldNames(String... modelFieldNames) {
        if (null != modelFieldNames && modelFieldNames.length > 0) {
            for (String modelFieldName : modelFieldNames) {
                FieldDef fieldDef = getFieldDefByModelFieldName(modelFieldName);
                if (null != fieldDef) {
                    customSelectFieldDefs.add(fieldDef);
                }
            }
        }
        return self();
    }


    public T addIgnoreCustomSelectModelFieldNames(String... modelFieldNames) {
        if (null != modelFieldNames && modelFieldNames.length > 0) {
            for (String modelFieldName : modelFieldNames) {
                FieldDef fieldDef = getFieldDefByModelFieldName(modelFieldName);
                if (null != fieldDef) {
                    customIgnoreSelectFieldDefs.add(fieldDef);
                }
            }
        }
        return self();
    }

    /**
     * 自定义要忽略的模型查询条件
     */
    private Set<Field> customIgnoreModelQueryConditionFieldSet = new HashSet<Field>();

    /**
     * 添加自定义要忽略的查询条件
     *
     * @param modelFieldNames java字段属性名称列表
     * @return Sql构造器本身
     */
    public T addIgnoreConditionField(String... modelFieldNames) {
        if (null != modelFieldNames && modelFieldNames.length > 0) {
            for (String modelFieldName : modelFieldNames) {
                FieldDef fieldDef = getFieldDefByModelFieldName(modelFieldName);
                if (null != fieldDef) {
                    customIgnoreModelQueryConditionFieldSet.add(fieldDef.getField());
                }
            }
        }
        return self();
    }

    /**
     * 是否要忽略这个模型属性的查询条件
     *
     * @param modelFieldDef 模型列定义
     * @return 是否自定义忽略
     */
    protected boolean isCustomIgnoreCondition(FieldDef modelFieldDef) {
        return customIgnoreModelQueryConditionFieldSet.contains(modelFieldDef.getField());
    }

    @Override
    protected String build() {
        // 构造 Where 语句
        buildWhereClause();

        // 构造 OrderBySql 排序属性
        buildOrderBySql();

        // 构造 LIMIT 子句
        buildLimitSql();

        // 构造要查询的列语句
        buildSelectColumnSql();

        // 构造不包含的 ORDER BY 和 LIMIT 子句的 FROM WHERE 语句
        StringBuilder sqlBuilder = buildFromWhereSql();

        return sqlBuilder.toString();
    }

    /**
     * 构造Where子句
     */
    private void buildWhereClause() {
        if (customWhereBuilder == null) {
            List<ConditionItem> conditionItemList = parseConditionItemList();
            if (CommonUtil.isNotEmpty(conditionItemList)) {
                for (ConditionItem conditionItem : conditionItemList) {
                    conditionItem.appendToWhereBuilder(getQueryCondition(), whereBuilder,
                            getCustomCompareType(conditionItem.getQueryConditionField()));
                }
            }
        }
    }

    protected abstract Object getQueryCondition();

    protected abstract List<ConditionItem> parseConditionItemList();

    private StringBuilder buildFromWhereSql() {
        WhereBuilder mainWhereBuilder = this.customWhereBuilder == null ? this.whereBuilder : this.customWhereBuilder;

        List<WhereBuilder> effectWhereBuilder = new ArrayList<>();
        effectWhereBuilder.add(mainWhereBuilder);
        effectWhereBuilder.add(this.subWhereBuilder);

        StringBuilder sqlBuilder = new StringBuilder(" FROM ").append(getWrapTableName());

        for (WhereBuilder whereBuilder : effectWhereBuilder) {
            if (whereBuilder != null && whereBuilder.conditionCount() > 0) {
                sqlBuilder.append(whereBuilder.getWhereSql());
                addListSqlParams(whereBuilder.getSqlParamList());
            }
        }
        return sqlBuilder;
    }

    /**
     * 构建 select 的列语句
     *
     * @return 列查询sql
     */
    private String buildSelectColumnSql() {

        this.selectColumnSql = null;

        if (StringUtils.isNotBlank(this.getCustomSelectColumnSql())) {
            this.selectColumnSql = getCustomSelectColumnSql();
        } else {

            boolean hasCustomIgnoreSelectColumn = CommonUtil.isNotEmpty(this.customIgnoreSelectFieldDefs);
            boolean hasCustomSelectColumn = CommonUtil.isNotEmpty(this.customSelectFieldDefs);

            if (!hasCustomIgnoreSelectColumn && !hasCustomSelectColumn) {
                this.selectColumnSql = "*";
            }

            Set<FieldDef> tempFieldDefs = new HashSet<FieldDef>();
            if (hasCustomIgnoreSelectColumn && !hasCustomSelectColumn) {
                tempFieldDefs.addAll(getMd().getAllDefList());
            } else {
                tempFieldDefs.addAll(this.customSelectFieldDefs);
            }

            if (CommonUtil.isNotEmpty(tempFieldDefs)) {
                StringBuilder builder = new StringBuilder();
                for (FieldDef fieldDef : tempFieldDefs) {
                    if (!this.customIgnoreSelectFieldDefs.contains(fieldDef)) {
                        builder.append(fieldDef.getColumnName()).append(" '").append(fieldDef.getFieldName()).append("',");
                    }
                }
                if (builder.length() > 0) {
                    builder.setLength(builder.length() - 1);
                    this.selectColumnSql = builder.toString();
                } else {
                    this.selectColumnSql = "*";
                }
                return this.selectColumnSql;
            } else {
                this.selectColumnSql = "*";
            }
        }

        if ("*".equals(this.selectColumnSql) && isExpandSelectColumn()) {
            List<FieldDef> fieldDefList = getMd().getAllDefList();
            StringBuilder builder = new StringBuilder();
            for (FieldDef fieldDef : fieldDefList) {
                if (!this.customIgnoreSelectFieldDefs.contains(fieldDef)) {
                    builder.append(wrapColumnName(fieldDef.getColumnName())).append(" '").append(fieldDef.getFieldName()).append("',");
                }
            }
            builder.setLength(builder.length() - 1);
            this.selectColumnSql = builder.toString();
        }

        return this.selectColumnSql;
    }

    private void buildLimitSql() {
        this.limitSql = "";
        if (JdbcHelper.isValidPagingParams(pageNo, pageSize)) {
            if (pageNo == 1) {
                this.limitSql = " LIMIT " + pageSize;
            } else {
                int pageOffset = (pageNo - 1) * pageSize;
                this.limitSql = " LIMIT " + pageOffset + "," + pageSize;
            }
        }
    }

    /**
     * 构造 OrderBy 语句
     */
    private void buildOrderBySql() {
        this.orderBySql = "";
        List<OrderItem> orderItemList = parseOrderItemList();
        if (CommonUtil.isNotEmpty(orderItemList)) {
            StringBuilder orderBySqlBuilder = new StringBuilder(" ORDER BY ");
            for (OrderItem orderItem : orderItemList) {
                String columnName = wrapColumnName(orderItem.getColumnName());
                orderBySqlBuilder.append(columnName).append(" ")
                        .append(orderItem.isAsc() ? "ASC" : "DESC").append(",");
            }
            orderBySqlBuilder.setLength(orderBySqlBuilder.length() - 1);
            this.orderBySql = orderBySqlBuilder.toString();
        }
    }

    /**
     * 自定义的字段比较类型
     */
    private Map<Field, CompareType> customCompareTypeMap = new HashMap<Field, CompareType>();

    public CompareType getCustomCompareType(Field queryConditionField) {
        AssertUtil.assertNotNull(queryConditionField, "要获取自定义比较类型的字段不存在！");
        return customCompareTypeMap.get(queryConditionField);
    }

    public T setCompareType(String modelFieldName, CompareType compareType) {
        AssertUtil.assertNotNull(modelFieldName, "要添加自定义查询比较类型的java属性名称不能为空！");
        FieldDef fieldDef = getFieldDefByModelFieldName(modelFieldName);
        AssertUtil.assertNotNull(fieldDef, "要添加自定义查询比较类型的java属性名称不存在！");
        customCompareTypeMap.put(fieldDef.getField(), compareType);
        return self();
    }

    /**
     * 获取查询SQL
     *
     * @return select sql
     */
    public String getSelectSql() {
        String fromWhereSql = getSql();
        String selectSql = "SELECT " + this.selectColumnSql + fromWhereSql + this.orderBySql + this.limitSql;
        logUsedSql(selectSql);
        return selectSql;
    }

    /**
     * 获取查询数量的SQL
     *
     * @return count sql
     */
    public String getCountSql() {
        String fromWhereSql = getSql();
        String countSql = "SELECT COUNT(*)" + fromWhereSql;
        logUsedSql(countSql);

        return countSql;
    }

    /**
     * 解析排序字段列表
     *
     * @return 返回需要排序的字段列表
     */
    protected abstract List<OrderItem> parseOrderItemList();
}
