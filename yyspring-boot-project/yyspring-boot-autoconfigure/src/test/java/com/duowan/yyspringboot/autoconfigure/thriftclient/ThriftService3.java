package com.duowan.yyspringboot.autoconfigure.thriftclient;

import com.duowan.common.thrift.client.annotation.ThriftResource;
import org.apache.thrift.TException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 15:53
 */
public class ThriftService3 {

    @ThriftResource
    private TestService.Iface testService;

    public void init() throws TException {
        System.out.println(testService);
        System.out.println("调用TestService.Iface.test(): " + testService.test(1000));
    }
}
