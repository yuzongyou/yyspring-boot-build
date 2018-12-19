package com.duowan.yyspringboot.autoconfigure.ipowner;

import com.duowan.common.ipowner.IpOwnerInfo;
import com.duowan.common.ipowner.IpOwnerService;
import com.duowan.common.utils.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author xiajiqiu
 * @version 1.0
 * @since 2018/9/9 21:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {IpOwnerAutoConfigurationTest.class, IpOwnerAutoConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ComponentScan("com.duowan.yyspringboot.autoconfigure.ipowner")
public class IpOwnerAutoConfigurationTest {

    @Autowired
    private IpOwnerService ipOwnerService;

    @Autowired
    private IpOwnerConsumer ipOwnerConsumer;

    @Test
    public void testIpOwner() {

        String ip = "58.248.229.154";

        IpOwnerInfo ipInfo = ipOwnerService.getIpOwnerInfo(ip, true, true, true);
        System.out.println(JsonUtil.toPrettyJson(ipInfo));
    }
}