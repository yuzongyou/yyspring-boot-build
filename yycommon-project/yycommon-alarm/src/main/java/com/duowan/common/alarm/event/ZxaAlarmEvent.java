package com.duowan.common.alarm.event;

/**
 * @author dw_xiajiqiu1
 */
public class ZxaAlarmEvent {

    private final String alarmNo;
    private final String message;

    public ZxaAlarmEvent(String alarmNo, String message) {
        this.alarmNo = alarmNo;
        this.message = message;
    }

    public String getAlarmNo() {
        return alarmNo;
    }

    public String getMessage() {
        return message;
    }
}
