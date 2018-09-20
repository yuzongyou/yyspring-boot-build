package com.duowan.common.thrift.server.schedule;

import com.duowan.common.thrift.server.exporter.ThriftExporter;

import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 17:50
 */
public class DefaultThriftExporterScheduler implements ThriftExporterScheduler {
    @Override
    public void setThriftExporters(List<ThriftExporter> exporters) {

    }

    @Override
    public List<ThriftExporterScheduleContext> schedule() {
        return null;
    }
}
