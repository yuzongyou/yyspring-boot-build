package com.duowan.yyspringcloud.msauth.app;

import com.duowan.common.utils.StringUtil;
import com.duowan.yyspringcloud.msauth.Constants;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 10:40
 */
public class EnvironmentAppReader implements AppReader, EnvironmentAware, Ordered {

    private Environment environment;

    public EnvironmentAppReader(Environment environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        if (this.environment == null) {
            throw new IllegalArgumentException("Environment should not be null");
        }
        this.environment = environment;
    }

    @Override
    public App read(String appId) {

        if (environment == null) {
            return null;
        }

        String secret = environment.getProperty(Constants.SECRET_KEY_PREFIX + appId);
        if (StringUtil.isBlank(secret)) {
            secret = environment.getProperty(Constants.DEFAULT_SECRET_KEY);
        }
        if (StringUtil.isBlank(secret)) {
            return null;
        }

        return new App(appId, secret);
    }

    @Override
    public int getOrder() {
        return Constants.ORDER_ENVIRONMENT_APP_READER;
    }
}
