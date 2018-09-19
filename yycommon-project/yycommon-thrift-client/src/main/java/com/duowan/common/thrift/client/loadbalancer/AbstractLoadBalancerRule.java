package com.duowan.common.thrift.client.loadbalancer;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 18:34
 */
public abstract class AbstractLoadBalancerRule implements Rule {

    protected LoadBalancer lb;

    @Override
    public void setLoadBalancer(LoadBalancer lb) {
        this.lb = lb;
    }

    @Override
    public LoadBalancer getLoadBalancer() {
        return this.lb;
    }
}
