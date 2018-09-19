package com.duowan.common.thrift.client.factory;

import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;
import org.apache.thrift.transport.TTransport;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 17:55
 */
public interface TTransportFactory {

    /**
     * TTransport 工厂接口
     *
     * @param clientConfig Thrift 客户端配置
     * @param serverNode   当前节点
     * @return 返回TTransport对象
     */
    TTransport create(TClientConfig clientConfig, ThriftServerNode serverNode);
}
