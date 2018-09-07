package com.duowan.logback.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/7 19:20
 */
public class ConsoleAppender extends ch.qos.logback.core.ConsoleAppender {

    @Override
    protected void append(Object eventObject) {
        super.append(eventObject);

        if (eventObject instanceof LoggingEvent) {
            LoggingEvent event = (LoggingEvent) eventObject;
            if (Level.ERROR.equals(event.getLevel())) {
                System.err.println("发送告警信息啦： " + event.getMessage());
            }
        }
    }
}
