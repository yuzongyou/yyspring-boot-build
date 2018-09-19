package com.duowan.common.thrift.client.loadbalancer;

import com.duowan.common.thrift.client.config.ThriftServerNode;
import com.duowan.common.thrift.client.exception.ServerNodeProviderNotFoundException;
import com.duowan.common.thrift.client.servernode.ServerNodeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 10:02
 */
public class DefaultLoadBalancer extends AbstractLoadBalancer {

    private final static Logger logger = LoggerFactory.getLogger(DefaultLoadBalancer.class);

    private Rule rule;

    private ServerNodeProvider serverNodeProvider;

    public DefaultLoadBalancer(ServerNodeProvider serverNodeProvider) {
        this(serverNodeProvider, null);
    }

    public DefaultLoadBalancer(ServerNodeProvider serverNodeProvider, Rule rule) {
        this.rule = rule;
        if (rule == null) {
            this.rule = new RoundRobinRule();
        }
        this.rule.setLoadBalancer(this);
        if (null == serverNodeProvider) {
            throw new ServerNodeProviderNotFoundException();
        }
        this.serverNodeProvider = serverNodeProvider;
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
                return serverNodeProvider.getServerNodes();
            } catch (Exception e) {
                logger.warn("从ServerNodeProvider获取Thrift服务节点失败: " + e.getMessage(), e);
            }
        }
        return nodeList;
    }
}
