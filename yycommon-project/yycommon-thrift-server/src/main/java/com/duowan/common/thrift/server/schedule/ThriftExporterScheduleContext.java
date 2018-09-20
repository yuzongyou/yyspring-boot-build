package com.duowan.common.thrift.server.schedule;

import com.duowan.common.thrift.server.exporter.ThriftExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 17:46
 */
public class ThriftExporterScheduleContext {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ThriftExporter exporter;

    private final ExecutorService executorService;

    private Thread scheduleThread;

    private long scheduleTime;

    public ThriftExporterScheduleContext(ExecutorService executorService, ThriftExporter exporter) {
        this.executorService = executorService;
        this.exporter = exporter;
    }

    public ExecutorService getExecutorService() {
        return executorService;
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

    public synchronized Thread publish() {

        if (scheduleThread != null) {
            return scheduleThread;
        } else {
            this.scheduleTime = System.currentTimeMillis();
            scheduleThread = new Thread(() -> {
                logger.info("发布Thrift服务: " + exporter);
                exporter.export();
            });
            executorService.execute(scheduleThread);
        }

        return scheduleThread;
    }
}
