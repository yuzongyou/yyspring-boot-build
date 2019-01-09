package com.duowan.yyspringboot.autoconfigure.mybatis;

import com.duowan.common.jdbc.Jdbc;
import com.duowan.yyspringboot.autoconfigure.jdbc.JdbcAutoConfiguration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Arvin
 * @version 1.0
 * @since 2019/1/8 15:56
 */
@Configuration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class, Jdbc.class})
@AutoConfigureAfter(JdbcAutoConfiguration.class)
@Import({MybatisMapperScannerRegistrar.class})
public class MybatisAutoConfiguration {

}
