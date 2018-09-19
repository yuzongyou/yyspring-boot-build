package com.duowan.yyspringboot.autoconfigure.thriftclient;

import com.duowan.common.thrift.client.annotation.ThriftResource;
import org.apache.thrift.TException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 15:53
 */
public class ThriftService {

    @ThriftResource("test1")
    private TestService.Iface testService;

    @ThriftResource("test2")
    private TestService2.Iface testService2;

    public void init() throws TException {
        System.out.println("调用TestService.Iface.test(): " + testService.test(1000));
        System.out.println("调用TestService2.Iface.test(): " + testService2.test(1000));
    }
}
