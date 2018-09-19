package com.duowan.yyspringboot.autoconfigure.thriftclient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 17:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ThriftClientAutoConfiguration.class, TClientConfigAutoConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ComponentScan("com.duowan.yyspringboot.autoconfigure.thriftclient")
public class ThriftClientAutoConfigurationTest {

    @Test
    public void test() {

        System.out.println("dddddddddd");
    }
}