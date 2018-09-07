package com.duowan.common.alarm.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import com.duowan.common.alarm.Alarm;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/7 19:52
 */
public class AlarmRollingFileAppender extends RollingFileAppender {

    @Override
    protected void append(Object eventObject) {
        super.append(eventObject);

        if (eventObject instanceof LoggingEvent) {
            LoggingEvent event = (LoggingEvent) eventObject;
            if (Level.ERROR.equals(event.getLevel())) {
                alarm(event);
            }
        }
    }

    protected void alarm(LoggingEvent event) {
        String message = event.getMessage();
        try {
            Alarm.alarm(message);
        } catch (Exception e) {
            System.err.println("发送告警信息失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
