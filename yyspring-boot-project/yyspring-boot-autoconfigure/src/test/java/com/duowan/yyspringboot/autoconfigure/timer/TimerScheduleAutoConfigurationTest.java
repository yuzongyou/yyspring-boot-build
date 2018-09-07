package com.duowan.yyspringboot.autoconfigure.timer;

import com.duowan.yyspringboot.autoconfigure.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/7 12:56
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TimerScheduleAutoConfiguration.class, TimerScheduleAutoConfigurationTest.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ComponentScan("com.duowan.yyspringboot.autoconfigure.timer")
public class TimerScheduleAutoConfigurationTest extends BaseTest {

    @Test
    public void timerScheduler() throws Exception {

        Thread.sleep(500);

        assertTrue(TestTimer.hadRunTestTimer);
    }

}