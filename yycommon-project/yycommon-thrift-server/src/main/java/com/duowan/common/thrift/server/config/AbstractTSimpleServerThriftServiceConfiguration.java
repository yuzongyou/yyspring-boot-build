package com.duowan.common.thrift.server.config;

import org.apache.thrift.server.TSimpleServer;

import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 23:37
 */
public class AbstractTSimpleServerThriftServiceConfiguration implements ThriftServiceConfiguration<TSimpleServer.Args> {

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public List<ThriftServiceItem> getServiceItemList() {
        return null;
    }

    @Override
    public void setThriftService(List<Object> thriftServiceList) {

    }

    @Override
    public boolean configureArgs(TSimpleServer.Args args, List<ThriftServiceItem> thriftServiceItemList) {
        return false;
    }
}
