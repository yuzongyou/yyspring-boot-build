package com.duowan.yyspringboot.autoconfigure.apollo;

import com.duowan.yyspring.boot.AppContext;
import com.duowan.yyspringboot.autoconfigure.SpringApplicationRunListenerAdapter;
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
public class ApolloConfigApplicationRunListener extends SpringApplicationRunListenerAdapter {

    private boolean needAutoConfigurer = true;

    public ApolloConfigApplicationRunListener(SpringApplication application, String[] args) {
        super(application, args);
        this.needAutoConfigurer = isClassesImported(
                "com.ctrip.framework.foundation.spi.provider.ApplicationProvider",
                "com.ctrip.framework.foundation.internals.DefaultProviderManager"
                );
    }

    @Override
    protected boolean needAutoConfigurer() {
        return needAutoConfigurer;
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
    protected void doStarting() {
        initialize();
    }

}
