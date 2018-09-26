package com.duowan.yyspringboot.autoconfigure.redis;

import com.duowan.common.redis.Redis;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/13 19:47
 */
@Configuration
@ConditionalOnClass({Redis.class})
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfiguration {

}
