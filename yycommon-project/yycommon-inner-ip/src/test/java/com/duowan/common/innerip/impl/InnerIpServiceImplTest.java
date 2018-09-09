package com.duowan.common.innerip.impl;

import com.duowan.common.innerip.InnerIpService;
import com.duowan.common.innerip.OfficialIp;
import org.junit.Before;
import org.junit.Test;

/**
 * @author xiajiqiu
 * @version 1.0
 * @since 2018/9/9 15:53
 */
public class InnerIpServiceImplTest {

    private InnerIpService innerIpService;

    @Before
    public void prepare() throws Exception {
        InnerIpServiceImpl innerIpService = new InnerIpServiceImpl();
        innerIpService.init();

        this.innerIpService = innerIpService;
    }

    @Test
    public void testIsOfficialIp() throws Exception {

        System.out.println(innerIpService.isOfficialIp("58.248.229.177"));

    }

    @Test
    public void testGetOfficialIp() throws Exception {

        OfficialIp officialIp = innerIpService.getOfficialIp("58.248.229.177");

        System.out.println(officialIp.getDesc());
        System.out.println(officialIp.getIp());
        System.out.println(officialIp.getAddTime());
    }
}