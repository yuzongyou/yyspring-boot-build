package com.duowan.common.thrift.client.loadbalancer;

import com.duowan.common.thrift.client.config.ThriftServerNode;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 18:28
 */
public interface Rule {

    /**
     * 设置 LoadBalancer
     *
     * @param lb loadBalancer
     */
    void setLoadBalancer(LoadBalancer lb);

    /**
     * 返回 LoadBalancer
     *
     * @return 返回 LoadBalancer
     */
    LoadBalancer getLoadBalancer();

    /**
     * 选择一个节点
     *
     * @param nodeId 节点ID
     * @return 返回
     */
    ThriftServerNode choose(Object nodeId);


}
