package com.duowan.yyspringboot.autoconfigure.alarm;

import com.duowan.common.alarm.Alarm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/7 20:20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AlarmAutoConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource("classpath:/com/duowan/yyspringboot/autoconfigure/alarm/application.properties")
public class AlarmAutoConfigurationTest {

    static {
        System.setProperty("PROJECTNO", "springboottest");
    }

    @Test
    public void testAlarm() throws InterruptedException {

        Alarm.alarm("test_alarm", "WarnTest");
        Alarm.alarm("WarnTest");

        Logger logger = LoggerFactory.getLogger("TESTLOG");
        logger.error("ErrorLogTest");

        Thread.sleep(1000);
    }

}