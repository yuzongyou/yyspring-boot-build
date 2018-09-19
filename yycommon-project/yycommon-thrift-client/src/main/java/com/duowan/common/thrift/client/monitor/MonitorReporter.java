package com.duowan.common.thrift.client.monitor;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/18 20:28
 */
public interface MonitorReporter {

    /**
     * 上报监控结果
     *
     * @param monitorResult 监控结果
     */
    void report(MonitorResult monitorResult);
}
