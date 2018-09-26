package com.duowan.yyspringboot.autoconfigure.jdbc;

import com.duowan.common.jdbc.Jdbc;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/13 12:50
 */
@Configuration
@ConditionalOnClass({Jdbc.class})
@EnableConfigurationProperties(JdbcProperties.class)
public class JdbcAutoConfiguration {

}
