package com.duowan.yyspringboot.autoconfigure.apollo;

import com.duowan.common.utils.StringUtil;
import com.duowan.yyspring.boot.AppContext;
import com.duowan.yyspring.boot.SpringApplicationRunListenerAdapter;
import org.springframework.boot.SpringApplication;

import java.lang.reflect.Field;

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
        if (StringUtil.isBlank(dwanv)) {
            dwanv = System.getProperty(dwanvKey);
        }
        if (StringUtil.isBlank(dwanv)) {
            System.setProperty(dwanvKey, AppContext.getEnv());
        }

        try {

            Class<?> foundationClass = Class.forName("com.ctrip.framework.foundation.Foundation");
            Field field = foundationClass.getDeclaredField("s_manager");
            field.setAccessible(true);

            Object instance = field.get(foundationClass);
            if (instance != null && instance.getClass() != Class.forName("com.ctrip.framework.foundation.internals.DefaultProviderManager")) {
                field.setAccessible(false);
                return;
            }

            field.set(foundationClass, new YYApolloProviderManager());
            field.setAccessible(false);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void doStarting() {
        super.doStarting();
        if (isFirstInit("constructor") && needAutoConfigurer()) {
            initialize();
        }
    }
}
