package com.duowan.common.jdbc;

import com.duowan.common.utils.JsonUtil;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
