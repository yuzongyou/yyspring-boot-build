package com.duowan.common.thrift.server.schedule;

import com.duowan.common.thrift.server.exporter.ThriftExporter;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 17:46
 */
public class ThriftExporterScheduleContext {

    private final ThriftExporter exporter;

    private final Thread scheduleThread;

    private final long scheduleTime;

    public ThriftExporterScheduleContext(ThriftExporter exporter, Thread scheduleThread) {
        this.exporter = exporter;
        this.scheduleThread = scheduleThread;
        this.scheduleTime = System.currentTimeMillis();
    }

    public ThriftExporter getExporter() {
        return exporter;
    }

    public Thread getScheduleThread() {
        return scheduleThread;
    }

    public long getScheduleTime() {
        return scheduleTime;
    }
}
