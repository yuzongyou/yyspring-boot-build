package com.duowan.yyspringboot.autoconfigure.thriftserver;

import com.duowan.common.thrift.server.exporter.ThriftServiceExporter;
import com.duowan.yyspringboot.autoconfigure.thriftserver.impl.HelloServiceImpl;
import com.duowan.yyspringboot.autoconfigure.thriftserver.impl.HiServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 19:26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ThriftServiceExporterAutoConfiguration.class, ThriftServiceExporterAutoConfigurationTest.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Configuration
@TestPropertySource("classpath:/com/duowan/yyspringboot/autoconfigure/thriftserver/application.properties")
public class ThriftServiceExporterAutoConfigurationTest {

    @Bean
    public HelloServiceImpl helloService() {
        return new HelloServiceImpl();
    }

    @Bean
    public HiServiceImpl hiService() {
        return new HiServiceImpl();
    }

    @Autowired(required = false)
    private HelloService helloService;

    @Autowired(required = false)
    private ThriftServiceExporter thriftServiceExporter;

    @Test
    public void testExport() {

        System.err.println("Export success!");

        System.err.println("ThriftServiceExporter: " + thriftServiceExporter);

        thriftServiceExporter.stop();
    }
}