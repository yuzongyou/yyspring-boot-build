package com.duowan.common.thrift.client.loadbalancer;

import com.duowan.common.thrift.client.config.ThriftServerNode;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 18:34
 */
public abstract class AbstractLoadBalancerRule implements Rule {

    protected LoadBalancer<? extends ThriftServerNode> lb;

    @Override
    public void setLoadBalancer(LoadBalancer<? extends ThriftServerNode> lb) {
        this.lb = lb;
    }

    @Override
    public LoadBalancer<? extends ThriftServerNode> getLoadBalancer() {
        return this.lb;
    }
}
