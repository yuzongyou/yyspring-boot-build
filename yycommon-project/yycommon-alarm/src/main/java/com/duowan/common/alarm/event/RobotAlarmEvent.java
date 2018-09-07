package com.duowan.common.alarm.event;

/**
 * @author dw_xiajiqiu1
 */
public class RobotAlarmEvent {

    private final String projectNo;

    private final String message;

    public RobotAlarmEvent(String projectNo, String message) {
        this.projectNo = projectNo;
        this.message = message;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public String getMessage() {
        return message;
    }
}
