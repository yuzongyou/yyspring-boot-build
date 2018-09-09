package com.duowan.yyspringboot.autoconfigure.innerip;

import com.duowan.common.innerip.InnerIpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author xiajiqiu
 * @version 1.0
 * @since 2018/9/9 16:01
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = InnerIpAutoConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class InnerIpAutoConfigurationTest {

    @Autowired
    private InnerIpService innerIpService;

    @Test
    public void testOfficialIp() {
        assertTrue(innerIpService.isOfficialIp("58.248.229.177"));
    }
}