package com.duowan.yyspringboot.autoconfigure.udbsecurity;

import com.duowan.common.utils.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/10 18:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UdbSecurityAutoConfiguration.class)
@TestPropertySource("classpath:/com/duowan/yyspringboot/autoconfigure/udbsecurity/application.properties")
public class UdbSecurityAutoConfigurationTest {

    @Autowired
    private ApplicationContext acx;

    @Test
    public void getUdbProperties() {

        try {
            UdbSecurityProperties udbProperties = acx.getBean(UdbSecurityProperties.class);

            System.out.println(JsonUtil.toPrettyJson(udbProperties));
        } catch (Exception e) {
            System.out.println("UdbProperties 不存在");
        }
    }
}