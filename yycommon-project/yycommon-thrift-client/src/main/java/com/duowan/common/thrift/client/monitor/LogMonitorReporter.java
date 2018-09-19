package com.duowan.common.thrift.client.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/18 20:29
 */
public class LogMonitorReporter implements MonitorReporter {

    private static final Logger logger = LoggerFactory.getLogger(LogMonitorReporter.class);

    @Override
    public void report(MonitorResult monitorResult) {
        if (monitorResult.isDown()) {
            logger.error(monitorResult.getMessage());
        }
    }
}
