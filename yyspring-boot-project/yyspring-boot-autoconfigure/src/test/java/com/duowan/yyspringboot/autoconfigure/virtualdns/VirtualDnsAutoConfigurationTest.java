package com.duowan.yyspringboot.autoconfigure.virtualdns;

import com.duowan.yyspringboot.autoconfigure.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.InetAddress;

import static org.junit.Assert.assertEquals;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/1 20:19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = VirtualDnsAutoConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource("classpath:/com/duowan/yyspringboot/autoconfigure/virtualdns/application.properties")
public class VirtualDnsAutoConfigurationTest extends BaseTest {

    @Test
    public void virtualDns() throws Exception {

        String ip = InetAddress.getByName("www.baidu.com").getHostAddress();
        assertEquals("127.0.0.1", ip);

        ip = InetAddress.getByName("www.google.com").getHostAddress();
        assertEquals("127.0.0.1", ip);
    }

}