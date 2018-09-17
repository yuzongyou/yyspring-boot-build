package com.duowan.common.thrift.client;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 17:44
 */
public interface Ordered {

    /**
     * 排序
     *
     * @return 返回序号
     */
    int getOrder();
}
