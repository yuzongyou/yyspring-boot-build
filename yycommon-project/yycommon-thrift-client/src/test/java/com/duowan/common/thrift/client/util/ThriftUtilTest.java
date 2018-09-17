package com.duowan.common.thrift.client.util;

import com.duowan.service.TestService;
import org.apache.thrift.TServiceClient;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 20:17
 */
public class ThriftUtilTest {
    @Test
    public void getTServiceClass() throws Exception {

        Class<? extends TServiceClient> clazz = ThriftUtil.getTServiceClass(TestService.class);

        System.out.println(clazz);

        assertEquals(TestService.Client.class, clazz);

    }

    @Test
    public void getIfaceClass() throws Exception {

        assertEquals(TestService.Iface.class, ThriftUtil.getIfaceClass(TestService.class));
    }

}