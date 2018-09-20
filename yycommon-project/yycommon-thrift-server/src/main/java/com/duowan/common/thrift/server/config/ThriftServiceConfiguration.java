package com.duowan.common.thrift.server.config;

import org.apache.thrift.server.TServer;

import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 22:05
 */
public interface ThriftServiceConfiguration<T extends TServer.AbstractServerArgs<T>> {

    /** 默认的 THRIFT 服务监听端口 */
    int DEFAULT_THRIFT_SERVER_PORT = 12000;

    /**
     * 服务发布端口，默认是12000
     *
     * @return 服务发布端口
     */
    int getPort();

    /**
     * 获取服务发布配置列表
     *
     * @return 要发布的服务项列表
     */
    List<ThriftServiceItem> getServiceItemList();

    /**
     * 设置 Thrift 服务实例列表
     *
     * @param thriftServiceList 实例列表
     */
    void setThriftService(List<Object> thriftServiceList);

    /**
     * 自定义配置发布参数
     *
     * @param args                  参数选项
     * @param thriftServiceItemList Thrift 服务实例项列表
     * @return 是否自动配置，自动配置会配置，transportFactory, protocolFactory, processor 为 TMultiplexedProcessor，返回 true的话表示完全由开发者自己配置发布的参数
     */
    boolean configureArgs(T args, List<ThriftServiceItem> thriftServiceItemList);
}
