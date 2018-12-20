package com.duowan.common.jdbc;

import com.duowan.common.jdbc.definition.OrderItem;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Arvin
 */
public abstract class AbstractOrderByQuery<Q> implements OrderByQuery {

    /**
     * 对应的实体模型类型
     */
    private final Class<?> modelType;

    /**
     * Order By 语句
     */
    private Set<OrderItem> orderBySet = new HashSet<>();

    public AbstractOrderByQuery(Class<?> modelType) {
        this.modelType = modelType;
    }

    @Override
    public Set<OrderItem> getOrderByItems() {
        return orderBySet;
    }

    /**
     * 设置排序字段, 默认是升序
     *
     * @param modelFieldName 模型类的属性名称
     * @return 返回查询对象本身
     */
    public Q orderBy(String modelFieldName) {
        return orderBy(0, modelFieldName, true);
    }

    public Q orderBy(String modelFieldName, boolean asc) {
        return orderBy(0, modelFieldName, asc);
    }

    /**
     * 指定顺序
     *
     * @param order          越小则排在越前
     * @param modelFieldName java属性名称
     * @param asc            是否升序
     * @return 返回查询对象本身
     */
    public Q orderBy(int order, String modelFieldName, boolean asc) {
        this.orderBySet.add(new OrderItem(order, this.modelType, modelFieldName, asc));
        return self();
    }

    protected abstract Q self();

    protected String filterBlankString(String value) {
        return null == value || "".equals(value.trim()) ? null : value.trim();
    }
}
