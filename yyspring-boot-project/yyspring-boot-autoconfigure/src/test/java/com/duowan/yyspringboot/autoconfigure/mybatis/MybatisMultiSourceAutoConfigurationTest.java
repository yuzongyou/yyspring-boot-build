package com.duowan.yyspringboot.autoconfigure.mybatis;

import com.duowan.yyspringboot.autoconfigure.jdbc.JdbcAutoConfiguration;
import com.duowan.yyspringboot.autoconfigure.mybatis.multids.ds1.Ds1Mapper;
import com.duowan.yyspringboot.autoconfigure.mybatis.multids.ds2.Ds2Mapper;
import com.duowan.yyspringboot.autoconfigure.virtualdns.VirtualDnsAutoConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

/**
 * @author Arvin
 * @version 1.0
 * @since 2019/1/8 16:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {
        JdbcAutoConfiguration.class,
        VirtualDnsAutoConfiguration.class,
        MybatisAutoConfiguration.class,
        MybatisMultiSourceAutoConfigurationTest.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource({"classpath:application-mybatis-multi-ds.properties"})
public class MybatisMultiSourceAutoConfigurationTest {

    @Autowired
    private ApplicationContext acx;

    @Autowired
    private Ds1Mapper ds1Mapper;

    @Test
    public void testMapper() {

        Ds1Mapper ds1Mapper = acx.getBean(Ds1Mapper.class);
        Ds2Mapper ds2Mapper = acx.getBean(Ds2Mapper.class);

        Assert.assertNotNull(ds1Mapper);
        Assert.assertNotNull(ds2Mapper);

        System.err.println(Arrays.toString(ds1Mapper.getDatabases().toArray()));
        System.err.println(Arrays.toString(ds1Mapper.getTables().toArray()));
        System.err.println(ds1Mapper.selectSelf("2"));

        System.err.println("--------------------------------------------------------");
        System.err.println(Arrays.toString(ds2Mapper.getDatabases().toArray()));
        System.err.println(Arrays.toString(ds2Mapper.getTables().toArray()));
        System.err.println(ds2Mapper.selectSelf("3"));


    }

}