package com.duowan.common.jdbc;

import com.duowan.common.utils.JsonUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Arvin
 */
public class JdbcSpringAutoRegister {

    @Test
    public void testInit() {

        System.setProperty("env", "dev");

        ApplicationContext acx = new ClassPathXmlApplicationContext("classpath:/applicationContext-test.xml");

        System.out.println("init......");

        String[] beanNames = acx.getBeanDefinitionNames();

        System.out.println(JsonUtil.toJson(beanNames));

    }

    @Test
    public void testRiseJdbc() {
        System.setProperty("env", "dev");

        ApplicationContext acx = new ClassPathXmlApplicationContext("classpath:/applicationContext-rise.xml");

        System.out.println("init......");

        String[] beanNames = acx.getBeanDefinitionNames();

        System.out.println(JsonUtil.toJson(beanNames));
    }
}
