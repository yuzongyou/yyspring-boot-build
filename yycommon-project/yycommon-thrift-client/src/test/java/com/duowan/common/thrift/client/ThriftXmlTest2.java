package com.duowan.common.thrift.client;

import com.duowan.common.thrift.client.annotation.ThriftResource;
import com.duowan.thrift.service.HelloService;
import com.duowan.thrift.service.HiService;
import org.apache.thrift.TException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 8:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/applicationContext.xml"})
public class ThriftXmlTest2 {

    @ThriftResource("hiService")
    private HiService.Iface hiService;

    @ThriftResource("helloService")
    private HelloService.Iface helloService;

    @Test
    public void test() throws TException {
        assertEquals(helloService.sayHello("Arvin"), "Hello, Arvin");
        assertEquals(hiService.sayHi("Arvin"), "Hi, Arvin");
    }
}
