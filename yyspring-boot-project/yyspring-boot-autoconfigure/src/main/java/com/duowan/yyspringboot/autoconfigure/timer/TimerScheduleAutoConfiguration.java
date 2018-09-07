package com.duowan.yyspringboot.autoconfigure.timer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定时任务自动调度自动配置
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/7 12:53
 */
@Configuration
@ConditionalOnClass({com.duowan.common.timer.Timer.class, com.duowan.common.timer.TimerUtil.class})
public class TimerScheduleAutoConfiguration {

    @Bean
    public TimerScheduler timerScheduler() {
        return new TimerScheduler();
    }

}
