package com.duowan.common.thrift.client.monitor;

import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/18 20:30
 */
public class TelnetNodeMonitor implements NodeMonitor {

    @Override
    public MonitorResult monitor(TClientConfig clientConfig, ThriftServerNode serverNode) {
        return null;
    }
}
