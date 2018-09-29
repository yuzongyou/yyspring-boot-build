package com.duowan.yyspringboot.autoconfigure.alarm;

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

}
