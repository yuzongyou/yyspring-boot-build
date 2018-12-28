package com.duowan.yyspringboot.autoconfigure.alarm;

import com.duowan.common.alarm.Alarm;
import com.duowan.common.alarm.subscriber.RobotAlarmHandler;
import com.duowan.common.alarm.subscriber.ZxaAlarmHandler;
import com.duowan.common.exception.CodeException;
import com.duowan.common.utils.ClassUtil;
import com.duowan.common.utils.StringUtil;
import com.duowan.yyspring.boot.AppContext;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/28 14:14
 */
public class AlarmHelper {

    private AlarmHelper() {
        throw new IllegalStateException("Helper class");
    }

    public static void initAlarm(AlarmProperties properties) {
        Alarm.init(AppContext.getProjectNo(), isEnabled(properties));

        // 注册告警信息处理器
        Alarm.registerAlarmHandler(new RobotAlarmHandler());

        if (StringUtil.isNotBlank(properties.getDsnUrl())) {
            if (isImportZxa()) {
                Alarm.registerAlarmHandler(new ZxaAlarmHandler(properties.getDsnUrl()));
            } else {
                throw new CodeException("设置了自定义告警编号 dsnUrl，但是没有引入zxa包，请先引入: \n<dependency>\n" +
                        "\t<groupId>com.duowan.bs.zxa</groupId>\n" +
                        "\t<artifactId>zxa</artifactId>\n" +
                        "</dependency>");
            }
        }
    }

    public static boolean isEnabled(AlarmProperties properties) {

        Boolean enabled = properties.getEnabled();
        if (enabled != null) {
            return enabled;
        }

        if (AppContext.isProd()) {
            return true;
        }
        System.err.println("当前环境为[" + AppContext.getEnv() + "]， 不是生产环境，不需要发送告警信息！");
        return false;
    }

    public static boolean isImportZxa() {
        return ClassUtil.isClassesImported("com.duowan.bs.zxa.Zxa");
    }
}
