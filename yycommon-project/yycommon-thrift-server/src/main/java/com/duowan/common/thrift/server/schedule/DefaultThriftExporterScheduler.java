package com.duowan.common.thrift.server.schedule;

import com.duowan.common.thrift.server.exporter.ThriftExporter;
import com.duowan.common.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 17:50
 */
public class DefaultThriftExporterScheduler implements ThriftExporterScheduler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private List<ThriftExporterScheduleContext> exporterScheduleContextList;

    private ExecutorService executorService;

    @Override
    public void setThriftExporters(List<ThriftExporter> exporters) {

        exporters = CommonUtil.filterDuplicateInstance(exporters);

        shutdownExecutorService();

        if (null == exporters || exporters.isEmpty()) {
            return;
        }

        executorService = Executors.newFixedThreadPool(exporterScheduleContextList.size());

        exporterScheduleContextList = new ArrayList<>();
        for (ThriftExporter exporter : exporters) {
            exporterScheduleContextList.add(new ThriftExporterScheduleContext(executorService, exporter));
        }
    }

    private void shutdownExecutorService() {
        try {
            if (null != executorService) {
                executorService.shutdownNow();
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public synchronized List<ThriftExporterScheduleContext> schedule() {

        if (null == exporterScheduleContextList || exporterScheduleContextList.isEmpty()) {
            logger.info("没有需要进行调度的Thrift服务！");
        } else {
            for (ThriftExporterScheduleContext context : exporterScheduleContextList) {
                context.publish();
            }
        }

        return exporterScheduleContextList;
    }
}
