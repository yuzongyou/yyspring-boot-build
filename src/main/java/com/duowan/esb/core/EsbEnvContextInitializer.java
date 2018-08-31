package com.duowan.esb.core;

import com.duowan.yyspring.boot.AppContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Arvin
 */
public class EsbEnvContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static {
        AppContext.prepareAppEnv();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        AppContext.setAcx(applicationContext);
    }
}
