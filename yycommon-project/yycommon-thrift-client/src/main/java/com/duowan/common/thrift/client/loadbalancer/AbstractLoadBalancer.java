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
public abstract class AbstractLoadBalancer<T extends ThriftServerNode> implements LoadBalancer<T> {

    private List<T> serverNodes = new ArrayList<>();

    @Override
    public void setServerNodes(List<T> serverNodes, boolean appendMode) {
        if (appendMode) {
            appendServerNodes(serverNodes);
        } else {
            resetServerNodes(serverNodes);
        }
    }

    private void appendServerNodes(List<T> serverNodes) {
        List<T> nodes = new ArrayList<>(serverNodes);
        if (serverNodes == null || serverNodes.isEmpty()) {
            throw new RuntimeException("None thrift nodes need append!");
        }
        nodes.addAll(serverNodes);
        this.serverNodes = new CopyOnWriteArrayList<>(serverNodes);
    }

    private void resetServerNodes(List<T> serverNodes) {
        this.serverNodes = new CopyOnWriteArrayList<>(serverNodes);
    }

    public T choose() {
        return chooseServerNode(null);
    }

    @Override
    public void offline(T serverNode) {
        if (null != serverNode) {
            serverNode.setAlive(false);
        }
    }

    @Override
    public boolean offline(String nodeId) {
        T node = chooseServerNode(nodeId);
        if (node != null) {
            offline(node);
            return true;
        }
        return false;
    }

    @Override
    public void up(T serverNode) {
        if (null != serverNode) {
            serverNode.setAlive(true);
        }
    }

    @Override
    public boolean up(String nodeId) {
        T node = chooseServerNode(nodeId);
        if (node != null) {
            up(node);
            return true;
        }
        return false;
    }

    @Override
    public List<T> getReachableServerNodes() {
        List<T> nodes = new ArrayList<>();
        for (T node : nodes) {
            if (node.isAlive()) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    @Override
    public List<T> getUnReachableServerNodes() {
        List<T> nodes = new ArrayList<>();
        for (T node : nodes) {
            if (!node.isAlive()) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    @Override
    public List<T> getAllServerNodes() {
        return new ArrayList<>(this.serverNodes);
    }
}
