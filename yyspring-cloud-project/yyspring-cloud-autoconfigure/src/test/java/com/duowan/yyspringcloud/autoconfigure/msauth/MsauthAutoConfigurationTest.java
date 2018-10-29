package com.duowan.yyspringcloud.autoconfigure.msauth;

import com.duowan.common.utils.JsonUtil;
import com.duowan.yyspringcloud.msauth.app.AppReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 14:19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MsauthAutoConfiguration.class,
        MsauthAutoConfigurationTest.class,
        HttpMessageConvertersAutoConfiguration.class,
        FeignAutoConfiguration.class})
@EnableFeignClients(basePackages = "com.duowan")
@TestPropertySource("classpath:application-msauth.properties")
public class MsauthAutoConfigurationTest {

    @Autowired
    private GithubService githubService;

    @Autowired
    private AppReader appReader;

    @Autowired
    private MsauthProperties properties;

    @Test
    public void test() {

        System.out.println(properties.getAppId());

        System.out.println(JsonUtil.toJson(appReader.read(properties.getAppId())));

        System.out.println(JsonUtil.toJson(appReader.read("unknown-app")));

        System.out.println(githubService.rateLimit());

    }

}