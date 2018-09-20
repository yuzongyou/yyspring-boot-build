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
public class FixedServerNodeDiscovery implements ServerNodeDiscovery {

    private final List<ThriftServerNode> serverNodeList;

    public FixedServerNodeDiscovery(String host, int port) {
        this.serverNodeList = new ArrayList<>(Collections.singletonList(new ThriftServerNode(host, port)));
    }

    public FixedServerNodeDiscovery(List<ThriftServerNode> serverNodeList) {
        this.serverNodeList = serverNodeList;
    }

    @Override
    public List<ThriftServerNode> getServerNodes() {
        return serverNodeList;
    }
}
