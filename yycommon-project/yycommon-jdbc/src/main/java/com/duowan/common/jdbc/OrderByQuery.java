package com.duowan.common.jdbc;

import com.duowan.common.jdbc.definition.OrderItem;

import java.util.Set;

/**
 * @author Arvin
 */
public interface OrderByQuery {

    /**
     * 获取排序的项目， 属性 + 升降序
     *
     * @return 返回排除项集合
     */
    Set<OrderItem> getOrderByItems();
}
