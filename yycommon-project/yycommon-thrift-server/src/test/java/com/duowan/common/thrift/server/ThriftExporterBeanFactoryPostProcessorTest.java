package com.duowan.common.thrift.server;

import com.duowan.common.thrift.server.exporter.ThriftServiceExporter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 17:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test1.xml")
public class ThriftExporterBeanFactoryPostProcessorTest {

    @Autowired
    private ApplicationContext acx;

    @Test
    public void test() {

        try {
            ThriftServiceExporter exporter = acx.getBean(ThriftServiceExporter.class);
            System.out.println(exporter);
        } catch (BeansException e) {
            System.err.println("ThriftServiceExporter 不存在！");
        }
    }
}