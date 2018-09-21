package com.duowan.yyspringboot.autoconfigure.thriftserver.impl;

import com.duowan.common.thrift.server.annotation.ThriftService;
import com.duowan.yyspringboot.autoconfigure.thriftserver.HiService;
import org.apache.thrift.TException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 22:48
 */
@ThriftService
public class HiServiceImpl implements HiService.Iface {
    @Override
    public void ping() throws TException {
    }

    @Override
    public String sayHi(String name) throws TException {
        return "Hi, " + name;
    }
}
