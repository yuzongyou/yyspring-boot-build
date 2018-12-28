package com.duowan.yyspringboot.autoconfigure.alarm;

import com.duowan.yyspring.boot.SpringApplicationRunListenerAdapter;
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
            AlarmHelper.initAlarm(properties);
        }
    }


}
