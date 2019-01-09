package com.duowan.yyspringboot.autoconfigure.mybatis;

import com.duowan.yyspringboot.autoconfigure.jdbc.JdbcAutoConfiguration;
import com.duowan.yyspringboot.autoconfigure.mybatis.singleds.SingleDsMapper;
import com.duowan.yyspringboot.autoconfigure.virtualdns.VirtualDnsAutoConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
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
        MybatisSingleSourceAutoConfigurationTest.class,
        JdbcAutoConfiguration.class,
        VirtualDnsAutoConfiguration.class,
        MybatisAutoConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource({"classpath:application-mybatis-single-ds.properties"})
public class MybatisSingleSourceAutoConfigurationTest {

    @Autowired
    private ApplicationContext acx;

    @Test
    public void testMapper() {

        SingleDsMapper mapper = null;

        try {
            mapper = acx.getBean(SingleDsMapper.class);
        } catch (BeansException e) {
            // ignored
        }

        Assert.assertNotNull(mapper);

        System.err.println(Arrays.toString(mapper.getDatabases().toArray()));
        System.err.println(Arrays.toString(mapper.getTables().toArray()));

        System.err.println(mapper.selectSelf("2"));


    }

}