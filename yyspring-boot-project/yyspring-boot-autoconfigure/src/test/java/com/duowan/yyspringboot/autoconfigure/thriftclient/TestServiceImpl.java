package com.duowan.yyspringboot.autoconfigure.thriftclient;

import org.apache.thrift.TException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 22:13
 */
public class TestServiceImpl implements TestService.Iface {
    @Override
    public String test(int length) throws TException {
        return "Hello, " + length;
    }
}
