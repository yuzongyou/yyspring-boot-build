package com.duowan.common.alarm;

import com.duowan.common.utils.AssertUtil;
import com.duowan.common.alarm.event.RobotAlarmEvent;
import com.duowan.common.alarm.event.ZxaAlarmEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dw_xiajiqiu1
 */
public class Alarm {

    private static final Logger logger = LoggerFactory.getLogger(Alarm.class);

    private static AsyncEventService eventBus = AsyncEventService.getInstance();

    private static boolean NEED_ALARM = false;

    private static String PROJECT_NO;

    public static String SYSTEM_ALARM;

    public static String MONITOR_ALARM;

    public static void init(String projectNo, boolean needAlarm) {
        AssertUtil.assertNotBlank(projectNo, "告警项目代号不能为空,请配置文件夹中设置[DWPROJECTNO]指定项目代号！");
        PROJECT_NO = projectNo;
        NEED_ALARM = needAlarm;
        SYSTEM_ALARM = PROJECT_NO + "_system_alarm";
        MONITOR_ALARM = PROJECT_NO + "_monitor_alarm";
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

        if (!NEED_ALARM) {
            logger.info("忽略发送告警[" + alarmNo + "][" + message + "]");
            return;
        }
        if (StringUtils.isBlank(alarmNo) || SYSTEM_ALARM.equalsIgnoreCase(alarmNo)) {
            eventBus.post(new RobotAlarmEvent(PROJECT_NO, message));
        } else {
            eventBus.post(new ZxaAlarmEvent(alarmNo, message));
        }
    }

    public static void alarm(String message) {
        alarm(null, message);
    }
}
