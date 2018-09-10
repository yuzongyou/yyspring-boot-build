package com.duowan.yyspring.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Arvin
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {YyEnvContextInitializerTest.class})
public class YyEnvContextInitializerTest {

    static {
        System.setProperty("DWENV", "dev");
        System.setProperty("PROJECTNO", "esb");
    }

    @Value("${test.name}")
    private String testName;

    @Value("${config.path}")
    private String configPath;

    @Test
    public void testInit() {

        System.out.println("testName...: " + testName);
        System.out.println("configPath...: " + configPath);
    }

}