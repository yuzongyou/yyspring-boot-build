package com.duowan.yyspringboot.autoconfigure.jdbc;

import com.duowan.common.jdbc.Jdbc;
import com.duowan.common.utils.JsonUtil;
import com.duowan.yyspring.boot.annotations.YYSpringBootApplication;
import com.duowan.yyspringboot.autoconfigure.virtualdns.VirtualDnsAutoConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/13 12:56
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {JdbcAutoConfigurationTest.class, JdbcAutoConfiguration.class, VirtualDnsAutoConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@YYSpringBootApplication(resourceLookupDirs = {"classpath:/com/duowan/yyspringboot/autoconfigure/jdbc/"})
//@TestPropertySource("classpath:/com/duowan/yyspringboot/autoconfigure/jdbc/application.properties")
public class JdbcAutoConfigurationTest {

    @Autowired
    private ApplicationContext acx;

    @Test
    public void testJdbc() {

        Map<String, Jdbc> beans = acx.getBeansOfType(Jdbc.class);

        System.out.println(beans);

        String sql = "show tables";

        for (Map.Entry<String, Jdbc> entry : beans.entrySet()) {
            Jdbc jdbc = entry.getValue();

            List<Map<String, Object>> mapList = jdbc.queryMapList(sql);

            System.out.println(JsonUtil.toPrettyJson(mapList));

            System.out.println("----------------------------------------------------------------------------");
        }

    }

}