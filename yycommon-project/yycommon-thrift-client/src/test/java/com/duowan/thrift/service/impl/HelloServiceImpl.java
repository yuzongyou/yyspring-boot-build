package com.duowan.thrift.service.impl;

import com.duowan.thrift.service.HelloService;
import org.apache.thrift.TException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 22:47
 */
public class HelloServiceImpl implements HelloService.Iface {
    @Override
    public void ping() throws TException { }

    @Override
    public String sayHello(String name) throws TException {
        return "Hello, " + name;
    }
}
