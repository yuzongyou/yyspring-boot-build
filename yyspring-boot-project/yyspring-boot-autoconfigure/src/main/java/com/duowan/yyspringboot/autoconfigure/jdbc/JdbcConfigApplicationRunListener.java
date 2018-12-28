package com.duowan.yyspringboot.autoconfigure.jdbc;

import com.duowan.yyspring.boot.SpringApplicationRunListenerAdapter;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/26 15:17
 */
public class JdbcConfigApplicationRunListener extends SpringApplicationRunListenerAdapter {

    private boolean needAutoConfigurer = false;

    public JdbcConfigApplicationRunListener(SpringApplication application, String[] args) {
        super(application, args);
        this.needAutoConfigurer = isClassesImported(
                "com.duowan.common.jdbc.Jdbc"
        );
    }

    @Override
    protected boolean needAutoConfigurer() {
        return needAutoConfigurer;
    }

    @Override
    protected void doContextPrepared(ConfigurableApplicationContext context, BeanDefinitionRegistry registry, ConfigurableEnvironment environment) {
        JdbcProperties jdbcProperties = bindProperties(JdbcProperties.PROPERTIES_PREFIX, JdbcProperties.class);

        if (null != jdbcProperties) {
            JdbcSpringRegister.registerJdbcBeans(jdbcProperties, registry, environment);
        }
    }
}
