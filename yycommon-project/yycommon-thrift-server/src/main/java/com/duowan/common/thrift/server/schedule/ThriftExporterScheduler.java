package com.duowan.common.thrift.server.schedule;

import com.duowan.common.thrift.server.exporter.ThriftExporter;

import java.util.List;

/**
 * 调度 ThriftExporter 进行 Thrift 服务的发布
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 17:38
 */
public interface ThriftExporterScheduler {

    /**
     * 设置 ThriftServiceExporter 列表
     *
     * @param exporters Thrift 服务导出列表
     */
    void setThriftExporters(List<ThriftExporter> exporters);

    /**
     * 调度 Thrift 服务
     *
     * @return 返回调度列表
     */
    List<ThriftExporterScheduleContext> schedule();

}
