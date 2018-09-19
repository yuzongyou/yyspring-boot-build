package com.duowan.common.thrift.client.loadbalancer;

import com.duowan.common.thrift.client.config.ThriftServerNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 18:37
 */
public abstract class AbstractLoadBalancer implements LoadBalancer {

    private List<ThriftServerNode> serverNodes = new ArrayList<>();

    @Override
    public void setServerNodes(List<ThriftServerNode> serverNodes, boolean appendMode) {
        if (appendMode) {
            appendServerNodes(serverNodes);
        } else {
            resetServerNodes(serverNodes);
        }
    }

    private void appendServerNodes(List<ThriftServerNode> serverNodes) {
        List<ThriftServerNode> nodes = new ArrayList<>(serverNodes);
        if (serverNodes == null || serverNodes.isEmpty()) {
            throw new RuntimeException("None thrift nodes need append!");
        }
        nodes.addAll(serverNodes);
        this.serverNodes = new CopyOnWriteArrayList<>(serverNodes);
    }

    private void resetServerNodes(List<ThriftServerNode> serverNodes) {
        this.serverNodes = new CopyOnWriteArrayList<>(serverNodes);
    }

    public ThriftServerNode choose() {
        return chooseServerNode(null);
    }

    @Override
    public void offline(ThriftServerNode serverNode) {
        if (null != serverNode) {
            serverNode.setAlive(false);
        }
    }

    @Override
    public boolean offline(String nodeId) {
        ThriftServerNode node = chooseServerNode(nodeId);
        if (node != null) {
            offline(node);
            return true;
        }
        return false;
    }

    @Override
    public void up(ThriftServerNode serverNode) {
        if (null != serverNode) {
            serverNode.setAlive(true);
        }
    }

    @Override
    public boolean up(String nodeId) {
        ThriftServerNode node = chooseServerNode(nodeId);
        if (node != null) {
            up(node);
            return true;
        }
        return false;
    }

    @Override
    public List<ThriftServerNode> getReachableServerNodes() {
        List<ThriftServerNode> nodes = getAllServerNodes();
        List<ThriftServerNode> reachableServerNodes = new ArrayList<>();
        for (ThriftServerNode node : nodes) {
            if (node.isAlive()) {
                reachableServerNodes.add(node);
            }
        }
        if (reachableServerNodes.isEmpty()) {
            return nodes;
        }
        return reachableServerNodes;
    }

    @Override
    public List<ThriftServerNode> getUnReachableServerNodes() {
        List<ThriftServerNode> nodes = getAllServerNodes();
        List<ThriftServerNode> unReachableServerNodes = new ArrayList<>();
        for (ThriftServerNode node : nodes) {
            if (!node.isAlive()) {
                unReachableServerNodes.add(node);
            }
        }
        return unReachableServerNodes;
    }

    @Override
    public List<ThriftServerNode> getAllServerNodes() {
        return this.serverNodes;
    }
}
