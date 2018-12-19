package com.duowan.common.alarm;

import com.duowan.common.alarm.event.RobotAlarmEvent;
import com.duowan.common.alarm.event.ZxaAlarmEvent;
import com.duowan.common.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author dw_xiajiqiu1
 */
public class Alarm {

    private Alarm() {
        throw new IllegalStateException("Utility class");
    }

    private static AsyncEventService eventBus = AsyncEventService.getInstance();

    private static boolean needAlarm = false;

    private static String projectNo;

    public static String systemAlarm;

    public static String monitorAlarm;

    public static void init(String projectNo, boolean needAlarm) {
        AssertUtil.assertNotBlank(projectNo, "告警项目代号不能为空,请配置文件夹中设置[DWPROJECTNO]指定项目代号！");
        Alarm.projectNo = projectNo;
        Alarm.needAlarm = needAlarm;
        systemAlarm = Alarm.projectNo + "_system_alarm";
        monitorAlarm = Alarm.projectNo + "_monitor_alarm";
    }

    /**
     * 注册告警处理器
     *
     * @param alarmHandler 告警处理器
     */
    public static void registerAlarmHandler(Object alarmHandler) {
        eventBus.register(alarmHandler);
    }

    public static void alarm(String alarmNo, String message) {

        if (!needAlarm) {
            System.err.println("[" + projectNo + "] 忽略发送告警[" + alarmNo + "][" + message + "]");
            return;
        }
        if (StringUtils.isBlank(alarmNo) || systemAlarm.equalsIgnoreCase(alarmNo)) {
            eventBus.post(new RobotAlarmEvent(projectNo, message));
        } else {
            eventBus.post(new ZxaAlarmEvent(alarmNo, message));
        }
    }

    public static void alarm(String message) {
        alarm(null, message);
    }
}
