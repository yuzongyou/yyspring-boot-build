package com.duowan.yyspringboot.autoconfigure.alarm;

import com.duowan.common.alarm.Alarm;
import com.duowan.common.alarm.subscriber.RobotAlarmHandler;
import com.duowan.common.alarm.subscriber.ZxaAlarmHandler;
import com.duowan.yyspring.boot.AppContext;
import com.duowan.yyspring.boot.SpringApplicationRunListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/29 10:33
 */
public class AlarmApplicationRunListener extends SpringApplicationRunListenerAdapter {

    private boolean needAutoConfigurer = true;

    public AlarmApplicationRunListener(SpringApplication application, String[] args) {
        super(application, args);
        this.needAutoConfigurer = isClassesImported("com.duowan.common.alarm.Alarm");
    }

    @Override
    protected boolean needAutoConfigurer() {
        return needAutoConfigurer;
    }

    @Override
    public void doEnvironmentPrepared(ConfigurableEnvironment environment) {
        AlarmProperties properties = bindProperties(AlarmProperties.PROPERTIES_PREFIX, AlarmProperties.class);

        if (null != properties) {
            initAlarm(properties);
        }
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
        System.err.println("当前环境为[" + AppContext.getEnv() + "]， 不是生产环境，不需要发送告警信息！");
        return false;
    }

    private boolean isImportZxa() {
        return isClassesImported("com.duowan.bs.zxa.Zxa");
    }

}
