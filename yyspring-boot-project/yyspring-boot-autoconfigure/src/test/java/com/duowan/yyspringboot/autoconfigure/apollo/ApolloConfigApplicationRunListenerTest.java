package com.duowan.yyspringboot.autoconfigure.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.duowan.common.utils.JsonUtil;
import com.duowan.yyspring.boot.annotations.YYSpringBootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

/**
 * @author xiajiqiu
 * @version 1.0
 * @since 2018/9/8 21:16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ApolloConfigApplicationRunListenerTest.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource("classpath:/com/duowan/yyspringboot/autoconfigure/apollo/application.properties")
@Import(ApolloConfig.class)
@YYSpringBootApplication(resourceLookupDirs = {"classpath:/com/duowan/yyspringboot/autoconfigure/apollo/"})
public class ApolloConfigApplicationRunListenerTest {

    @Autowired
    private TokenConfigBean tokenConfig;

    @Autowired
    private Oauth2YyConfig oauth2YyConfig;

    @Test
    public void testFetchConfig() {

        System.out.println(JsonUtil.toPrettyJson(tokenConfig));
        System.out.println(JsonUtil.toPrettyJson(oauth2YyConfig));

        Config config = ConfigService.getConfig("qp-oauth2-yy");

        Set<String> names = config.getPropertyNames();
        for (String name : names) {
            System.out.println(name + " : [" + config.getProperty(name, null) + "]");
        }
    }
}