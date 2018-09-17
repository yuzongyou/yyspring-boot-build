package com.duowan.common.thrift.client.monitor;

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
     * @param serverNode 服务节点
     * @param transport  transport 对象，可以为空，如果为空的话会自己创建一个
     * @return 返回监控结果
     */
    MonitorResult monitor(ThriftServerNode serverNode, TTransport transport);
}
