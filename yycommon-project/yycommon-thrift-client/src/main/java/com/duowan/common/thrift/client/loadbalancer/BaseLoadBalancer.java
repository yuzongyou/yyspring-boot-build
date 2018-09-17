package com.duowan.common.thrift.client.loadbalancer;

import com.duowan.common.thrift.client.config.ThriftServerNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 18:53
 */
public class BaseLoadBalancer extends AbstractLoadBalancer {

    private static Logger logger = LoggerFactory.getLogger(BaseLoadBalancer.class);

    private final static Rule DEFAULT_RULE = new RoundRobinRule();

    private Rule rule = DEFAULT_RULE;

    @Override
    public ThriftServerNode chooseServerNode(String nodeId) {
        return null;
    }
}
