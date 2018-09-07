package com.duowan.common.alarm.subscriber;

import com.duowan.bs.zxa.Dsn;
import com.duowan.bs.zxa.Zxa;
import com.duowan.bs.zxa.ZxaFactory;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.alarm.event.ZxaAlarmEvent;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dw_xiajiqiu1
 */
public class ZxaAlarmHandler {

    private static final Logger logger = LoggerFactory.getLogger(ZxaAlarmHandler.class);

    private final Zxa zxa;

    private final String systemAlarmNo;

    /**
     * 设置 DSN， DNS 可以在 升龙警报系统 --&lt; 客户端接入 --&lt; Java 的代码中查看
     *
     * @param dsnUrl 类似 http://bbs_antispam:d6336878769ea56cdfed@alarm.defensor.game.yy.com:80?DWENV=prod
     */
    public ZxaAlarmHandler(String dsnUrl) {
        AssertUtil.assertNotBlank(dsnUrl, "升龙ZXA告警 DSN 不能为空！");
        Dsn dsn = new Dsn(dsnUrl);
        zxa = ZxaFactory.newInstance(dsn);
        this.systemAlarmNo = dsn.getAppId() + "_system_alarm";
    }

    @Subscribe
    public void onZxaAlarm(ZxaAlarmEvent event) {
        String message = event.getMessage();
        if (StringUtils.isBlank(message)) {
            return;
        }
        String alarmNo = event.getAlarmNo();
        if (StringUtils.isBlank(alarmNo)) {
            alarmNo = this.systemAlarmNo;
        }

        Exception exception = null;
        try {
            zxa.sendMessage(alarmNo, message);
        } catch (Exception e) {
            exception = e;
        } finally {
            String status = exception == null ? "成功" : "失败";
            String errorMessage = exception == null ? "" : ", 上报错误信息: [" + exception.getMessage() + "]";
            String content = "发送告警 " + status + ", AlarmNo[" + alarmNo + "],告警内容：[" + message + "]" + errorMessage;
            if (null == exception) {
                logger.info(content);
            } else {
                logger.info(content, exception);
            }
        }

    }
}
