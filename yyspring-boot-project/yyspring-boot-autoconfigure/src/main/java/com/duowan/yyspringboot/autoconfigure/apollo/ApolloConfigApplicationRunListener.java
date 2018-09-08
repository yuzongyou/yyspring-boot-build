package com.duowan.yyspringboot.autoconfigure.apollo;

import com.duowan.yyspring.boot.AppContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/8 20:36
 */
public class ApolloConfigApplicationRunListener implements SpringApplicationRunListener, Ordered {

    private final SpringApplication application;

    private final String[] args;

    public ApolloConfigApplicationRunListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    private void initialize() {
        // 重新设置环境变量
        String dwanvKey = "DWENV";
        String dwanv = System.getenv(dwanvKey);
        if (StringUtils.isBlank(dwanv)) {
            dwanv = System.getProperty(dwanvKey);
        }
        if (StringUtils.isBlank(dwanv)) {
            System.setProperty(dwanvKey, AppContext.getEnv());
        }
    }

    @Override
    public void starting() {
        initialize();
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }

    @Override
    public int getOrder() {
        // 必须在 com.duowan.yyspring.boot.YySpringApplicationRunListener 之后执行
        return 2;
    }
}
