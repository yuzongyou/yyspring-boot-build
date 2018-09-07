package com.duowan.yyspringboot.autoconfigure.alarm;

import com.duowan.common.alarm.Alarm;
import com.duowan.common.alarm.subscriber.RobotAlarmHandler;
import com.duowan.common.alarm.subscriber.ZxaAlarmHandler;
import com.duowan.yyspring.boot.AppContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/7 20:05
 */
@Configuration
@ConditionalOnClass({com.duowan.common.alarm.Alarm.class})
@EnableConfigurationProperties(AlarmProperties.class)
public class AlarmAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(AlarmAutoConfiguration.class);

    public AlarmAutoConfiguration(AlarmProperties properties) {
        initAlarm(properties);
    }

    private void initAlarm(AlarmProperties properties) {
        Alarm.init(AppContext.getProjectNo(), isEnabled(properties));

        // 注册告警信息处理器
        Alarm.registerAlarmHandler(new RobotAlarmHandler());

        if (StringUtils.isNotBlank(properties.getDsnUrl())) {
            if (isImportZxa()) {
                Alarm.registerAlarmHandler(new ZxaAlarmHandler(properties.getDsnUrl()));
            } else {
                throw new RuntimeException("设置了自定义告警编号 dsnUrl，但是没有引入zxa包，请先引入: \n<dependency>\n" +
                        "\t<groupId>com.duowan.bs.zxa</groupId>\n" +
                        "\t<artifactId>zxa</artifactId>\n" +
                        "</dependency>");
            }
        }
    }

    private boolean isEnabled(AlarmProperties properties) {

        Boolean enabled = properties.getEnabled();
        if (enabled != null) {
            return enabled;
        }

        if (AppContext.isProd()) {
            return true;
        }
        logger.info("当前环境为[" + AppContext.getEnv() + "]， 不是生产环境，不需要发送告警信息！");
        return false;
    }

    private boolean isImportZxa() {
        try {
            Class.forName("com.duowan.bs.zxa.Zxa");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
