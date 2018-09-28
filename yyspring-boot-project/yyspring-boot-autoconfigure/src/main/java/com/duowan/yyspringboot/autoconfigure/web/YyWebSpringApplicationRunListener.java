package com.duowan.yyspringboot.autoconfigure.web;

import com.duowan.yyspring.boot.AppContext;
import com.duowan.yyspringboot.autoconfigure.SpringApplicationRunListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/10 13:31
 */
public class YyWebSpringApplicationRunListener extends SpringApplicationRunListenerAdapter {

    public YyWebSpringApplicationRunListener(SpringApplication application, String[] args) {
        super(application, args);
    }

    @Override
    public void starting() {
        WebApplicationType webApplicationType = getApplication().getWebApplicationType();
        if (WebApplicationType.REACTIVE.equals(webApplicationType) || WebApplicationType.SERVLET.equals(webApplicationType)) {
            if (AppContext.isDev()) {
                WebPrepareUtil.prepareStaticResourceLocations(AppContext.getEnvironment(), AppContext.getModuleDir());
                WebPrepareUtil.prepareThymeleafDevConfig(AppContext.getEnvironment(), AppContext.getModuleDir());
            }
        }
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        fixServerPort(environment);
    }

    public static final String DEFAULT_SERVER_PORT = "8081";

    private static void fixServerPort(ConfigurableEnvironment environment) {

        String serverPortKey = "server.port";
        String serverPort = environment.getProperty(serverPortKey);
        if (StringUtils.isNotBlank(serverPort)) {
            AppContext.getApplicationProperties().put(serverPortKey, serverPort);
        }

        if (!AppContext.getApplicationProperties().containsKey(serverPortKey)) {
            // 默认使用 PROD:8081
            AppContext.getApplicationProperties().put(serverPortKey, DEFAULT_SERVER_PORT);
        }
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        Logger webPrepareUtilLogger = LoggerFactory.getLogger(YyWebSpringApplicationRunListener.class);
        List<String> infoList = WebPrepareUtil.getInitInfo();
        for (String info : infoList) {
            webPrepareUtilLogger.info(info);
        }
    }
}
