package com.duowan.common.thrift.client.loadbalancer;

import com.duowan.common.thrift.client.config.ThriftServerNode;
import com.duowan.common.thrift.client.exception.ServerNodeDiscoveryNotFoundException;
import com.duowan.common.thrift.client.servernode.ServerNodeDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 10:02
 */
public class DefaultLoadBalancer extends AbstractLoadBalancer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultLoadBalancer.class);

    private Rule rule;

    private ServerNodeDiscovery serverNodeDiscovery;

    public DefaultLoadBalancer(ServerNodeDiscovery serverNodeDiscovery) {
        this(serverNodeDiscovery, null);
    }

    public DefaultLoadBalancer(ServerNodeDiscovery serverNodeDiscovery, Rule rule) {
        this.rule = rule;
        if (rule == null) {
            this.rule = new RoundRobinRule();
        }
        this.rule.setLoadBalancer(this);
        if (null == serverNodeDiscovery) {
            throw new ServerNodeDiscoveryNotFoundException();
        }
        this.serverNodeDiscovery = serverNodeDiscovery;
    }

    @Override
    public ThriftServerNode chooseServerNode(String nodeId) {
        return rule.choose(nodeId);
    }

    @Override
    public List<ThriftServerNode> getAllServerNodes() {
        List<ThriftServerNode> nodeList = super.getAllServerNodes();
        if (nodeList == null || nodeList.isEmpty()) {
            try {
                return serverNodeDiscovery.getServerNodes();
            } catch (Exception e) {
                LOGGER.warn("从ServerNodeDiscovery获取Thrift服务节点失败: " + e.getMessage(), e);
            }
        }
        return nodeList;
    }
}
