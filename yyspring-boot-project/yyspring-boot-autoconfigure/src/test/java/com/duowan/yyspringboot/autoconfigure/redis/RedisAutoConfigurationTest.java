package com.duowan.yyspringboot.autoconfigure.redis;

import com.duowan.common.redis.JedisExecutor;
import com.duowan.common.redis.Redis;
import com.duowan.yyspringboot.autoconfigure.virtualdns.VirtualDnsAutoConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/13 20:04
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {RedisAutoConfiguration.class, VirtualDnsAutoConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource("classpath:/com/duowan/yyspringboot/autoconfigure/redis/application.properties")
public class RedisAutoConfigurationTest {

    @Autowired
    private ApplicationContext acx;

    @Test
    public void testRedis() {
        Map<String, Redis> beans = acx.getBeansOfType(Redis.class);

        System.out.println(beans);

        if (null == beans || beans.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Redis> entry : beans.entrySet()) {
            Redis redis = entry.getValue();

            Long dbSize = redis.execute((JedisExecutor<Long>) jedis -> jedis.dbSize());
            System.out.println("DBSIZE: " + dbSize);

            System.out.println("----------------------------------------------------------------------------");
        }
    }
}