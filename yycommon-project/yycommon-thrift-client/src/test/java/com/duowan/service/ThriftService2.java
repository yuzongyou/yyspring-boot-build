package com.duowan.service;

import com.duowan.common.thrift.client.annotation.ThriftResource;
import org.apache.thrift.TException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 15:53
 */
public class ThriftService2 {

    @ThriftResource("test1")
    private TestService.Iface testService;

    private TestService.Iface testService2;

    @ThriftResource("test1")
    public void setTestService2(TestService.Iface testService2) {
        this.testService2 = testService2;
    }

    public void init() throws TException {
        System.out.println("调用TestService.Iface.test(): " + testService.test(1000));
    }
}
