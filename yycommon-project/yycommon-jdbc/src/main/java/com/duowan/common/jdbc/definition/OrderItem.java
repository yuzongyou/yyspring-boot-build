package com.duowan.common.jdbc.definition;

import com.duowan.common.jdbc.FieldDef;
import com.duowan.common.jdbc.MDContext;
import com.duowan.common.utils.AssertUtil;

/**
 * @author Arvin
 */
public class OrderItem implements Comparable<OrderItem> {

    /**
     * 越小越靠前
     */
    private int order;
    /**
     * 要排序的列定义
     */
    private FieldDef fieldDef;
    private boolean asc;

    public OrderItem(Class<?> modelType, String modelFieldName) {
        this(0, modelType, modelFieldName, true);
    }

    public OrderItem(Class<?> modelType, String modelFieldName, boolean asc) {
        this(0, modelType, modelFieldName, asc);
    }

    public OrderItem(int order, Class<?> modelType, String modelFieldName, boolean asc) {
        AssertUtil.assertNotNull(modelType, "排序项初始化 必须提供 模型类型！");
        AssertUtil.assertNotBlank(modelFieldName, "排序项初始化 必须提供要进行排序的 java 属性名称");

        this.order = order;

        this.fieldDef = MDContext.getMD(modelType)
                .getFieldDefByModelFieldName(modelFieldName);

        this.asc = asc;
    }

    public OrderItem(String columnName, Class<?> modelType) {
        this(0, columnName, modelType, true);
    }

    public OrderItem(String columnName, Class<?> modelType, boolean asc) {
        this(0, columnName, modelType, asc);
    }

    public OrderItem(int order, String columnName, Class<?> modelType, boolean asc) {
        AssertUtil.assertNotNull(modelType, "排序项初始化 必须提供 模型类型！");
        AssertUtil.assertNotBlank(columnName, "排序项初始化 必须提供要进行排序的 columnName 名称");

        this.order = order;

        this.fieldDef = MDContext.getMD(modelType)
                .getFieldDefByColumn(columnName);

        this.asc = asc;
    }

    public int getOrder() {
        return order;
    }

    public FieldDef getFieldDef() {
        return fieldDef;
    }

    public String getColumnName() {
        return this.fieldDef.getColumnName();
    }

    public String getModelFieldName() {
        return this.fieldDef.getFieldName();
    }

    public boolean isAsc() {
        return asc;
    }

    @Override
    public int compareTo(OrderItem o) {
        if (this.order == o.order) {
            return 0;
        }
        int ret = this.order - o.order;
        if (0 == ret) {
            return 0;
        }
        return ret > 0 ? 1 : -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderItem orderItem = (OrderItem) o;

        return !(fieldDef != null ? !fieldDef.equals(orderItem.fieldDef) : orderItem.fieldDef != null);

    }

    @Override
    public int hashCode() {
        return fieldDef != null ? fieldDef.hashCode() : 0;
    }
}
