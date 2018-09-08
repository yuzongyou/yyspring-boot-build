package com.duowan.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.duowan.common.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * @author xiajiqiu
 * @version 1.0
 * @since 2018/9/8 21:44
 */
@SpringBootApplication
@Component
public class ApolloApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApolloApplication.class, args);
    }

    @Autowired
    private TokenConfigBean tokenConfig;

    @Autowired
    private Oauth2YyConfig oauth2YyConfig;

    @PostConstruct
    public void init() {

        System.out.println(JsonUtil.toPrettyJson(tokenConfig));
        System.out.println(JsonUtil.toPrettyJson(oauth2YyConfig));

        Config config = ConfigService.getConfig("qp-oauth2-yy");

        Set<String> names = config.getPropertyNames();
        for (String name : names) {
            System.out.println(name + " : [" + config.getProperty(name, null) + "]");
        }
    }
}
