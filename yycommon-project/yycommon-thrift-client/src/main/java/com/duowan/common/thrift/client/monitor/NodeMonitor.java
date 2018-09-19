package com.duowan.common.thrift.client.monitor;

import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;
import org.apache.thrift.transport.TTransport;

/**
 * Thrift Server Node 监控
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 18:56
 */
public interface NodeMonitor {

    /**
     * 监控Thrift Server 节点
     *
     * @param clientConfig Thrift Client Config
     * @param serverNode   服务节点
     * @return 返回监控结果
     */
    MonitorResult monitor(TClientConfig clientConfig, ThriftServerNode serverNode);
}
