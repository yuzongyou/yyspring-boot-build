package com.duowan.common.thrift.client;

import com.duowan.service.TestService;
import com.duowan.service.TestService2;
import org.apache.thrift.TException;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 13:33
 */
public class ThriftXmlTest {

    @Test
    public void testInitByApplicationContext() throws TException, InterruptedException {

        ApplicationContext acx = new ClassPathXmlApplicationContext("classpath:/applicationContext-thrift-client-test.xml");

        System.out.println("init......");

        String[] beanNames = acx.getBeanDefinitionNames();

        System.out.println(Arrays.toString(beanNames));

        TestService.Iface testService = acx.getBean(TestService.Iface.class);

        System.out.println(testService.test(1000));

        TestService2.Iface test2Service = acx.getBean(TestService2.Iface.class);

        System.out.println(test2Service.test(1000));

    }

    @Test
    public void testInitByApplicationContext2() throws TException, InterruptedException {

        ApplicationContext acx = new ClassPathXmlApplicationContext("classpath:/applicationContext-thrift-client-test2.xml");

        System.out.println("init......");

        String[] beanNames = acx.getBeanDefinitionNames();

        System.out.println(Arrays.toString(beanNames));

        TestService.Iface testService = acx.getBean(TestService.Iface.class);

        System.out.println(testService.test(1000));

    }

}
