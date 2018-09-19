package com.duowan.common.thrift.client.servernode;

import com.duowan.common.thrift.client.config.ThriftServerNode;

import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/18 15:45
 */
public interface ServerNodeProvider {

    /**
     * 获取Thrift 服务节点列表
     *
     * @return 返回节点列表
     */
    List<ThriftServerNode> getServerNodes();
}
