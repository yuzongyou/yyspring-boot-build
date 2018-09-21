package com.duowan.yyspringboot.autoconfigure.thriftserver.impl;

import com.duowan.common.thrift.server.annotation.ThriftService;
import com.duowan.yyspringboot.autoconfigure.thriftserver.HelloService;
import org.apache.thrift.TException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 22:47
 */
@ThriftService
public class HelloServiceImpl implements HelloService.Iface {
    @Override
    public void ping() throws TException { }

    @Override
    public String sayHello(String name) throws TException {
        return "Hello, " + name;
    }
}
