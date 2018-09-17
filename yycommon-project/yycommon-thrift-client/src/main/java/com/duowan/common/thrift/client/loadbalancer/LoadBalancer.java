package com.duowan.common.thrift.client.loadbalancer;

import com.duowan.common.thrift.client.config.ThriftServerNode;

import java.util.List;

/**
 * 负载均衡
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 18:12
 */
public interface LoadBalancer<T extends ThriftServerNode> {

    /**
     * 设置服务节点
     *
     * @param serverNodes 要设置的服务节点
     * @param appendMode  追加模式， 如果为 true 则追加，否则进行完全替换
     */
    void setServerNodes(List<T> serverNodes, boolean appendMode);

    /**
     * 选择指定ID的服务节点
     *
     * @param nodeId 节点ID
     * @return 返回给定的服务节点，如果不存在这返回 null
     */
    T chooseServerNode(String nodeId);

    /**
     * 下线给定服务节点
     *
     * @param serverNode 要下线的节点
     */
    void offline(T serverNode);

    /**
     * 下线指定ID的服务节点
     *
     * @param nodeId 节点ID
     * @return 如果节点存在这返回true， 否则返回false
     */
    boolean offline(String nodeId);

    /**
     * 上线给定服务节点
     *
     * @param serverNode 要下线的节点
     */
    void up(T serverNode);

    /**
     * 上线指定ID的服务节点
     *
     * @param nodeId 节点ID
     * @return 如果节点存在这返回true， 否则返回false
     */
    boolean up(String nodeId);

    /**
     * 获取可用的服务节点列表
     *
     * @return 返回可用的节点列表，始终返回非 null
     */
    List<T> getReachableServerNodes();

    /**
     * 获取不可用的服务节点列表
     *
     * @return 返回不可用的节点列表，始终返回非 null
     */
    List<T> getUnReachableServerNodes();

    /**
     * 获取所有的服务节点列表
     *
     * @return 返回所有服务节点列表，始终返回非 null
     */
    List<T> getAllServerNodes();

}
