package com.duowan.common.thrift.client.servernode;

import com.duowan.common.thrift.client.config.ThriftServerNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/18 15:47
 */
public class FixedServerNodeProvider implements ServerNodeProvider {

    private final List<ThriftServerNode> serverNodeList;

    public FixedServerNodeProvider(String host, int port) {
        this.serverNodeList = new ArrayList<>(Collections.singletonList(new ThriftServerNode(host, port)));
    }

    public FixedServerNodeProvider(List<ThriftServerNode> serverNodeList) {
        this.serverNodeList = serverNodeList;
    }

    @Override
    public List<ThriftServerNode> getServerNodes() {
        return serverNodeList;
    }
}
