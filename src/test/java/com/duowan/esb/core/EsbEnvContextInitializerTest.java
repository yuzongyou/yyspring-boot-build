package com.duowan.esb.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author Arvin
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EsbEnvContextInitializerTest.class)
@Import({EsbEnvContextInitializer.class})
public class EsbEnvContextInitializerTest {

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