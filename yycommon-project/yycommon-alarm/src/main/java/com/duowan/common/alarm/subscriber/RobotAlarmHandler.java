package com.duowan.common.alarm.subscriber;

import com.duowan.common.utils.HttpUtil;
import com.duowan.common.utils.IpAddressUtils;
import com.duowan.common.utils.RetryUtil;
import com.duowan.common.alarm.event.RobotAlarmEvent;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dw_xiajiqiu1
 */
public class RobotAlarmHandler {

    private static final Logger logger = LoggerFactory.getLogger(RobotAlarmHandler.class);

    private static final String REPORT_URL = "http://robot.leopard.yy.com/webservice/errorlog/log.do";

    @Subscribe
    public void onRobotAlarm(RobotAlarmEvent event) {
        String message = event.getMessage();
        if (StringUtils.isBlank(message)) {
            return;
        }
        String projectNo = event.getProjectNo();
        if (StringUtils.isBlank(projectNo)) {
            return;
        }
        final Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("code", projectNo);
        paramsMap.put("level", "error");
        paramsMap.put("ip", IpAddressUtils.getLocalIp());
        paramsMap.put("version", "1.1.1");
        paramsMap.put("message", message);

        Exception exception = null;
        try {
            RetryUtil.execute(3, 1000, true, () -> {
                HttpUtil.doPost(REPORT_URL, paramsMap, null, 3000, 3000);
                return null;
            });
        } catch (Exception e) {
            exception = e;
        } finally {
            String status = exception == null ? "成功" : "失败";
            String errorMessage = exception == null ? "" : ", 上报错误信息: [" + exception.getMessage() + "]";
            String content = "发送告警 " + status + ", 告警内容：[" + message + "]" + errorMessage;
            if (null == exception) {
                logger.info(content);
            } else {
                logger.info(content, exception);
            }
        }

    }
}
