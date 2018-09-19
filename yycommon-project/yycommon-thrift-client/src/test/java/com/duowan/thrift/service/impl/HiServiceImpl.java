package com.duowan.thrift.service.impl;

import com.duowan.thrift.service.HiService;
import org.apache.thrift.TException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 22:48
 */
public class HiServiceImpl implements HiService.Iface {
    @Override
    public void ping() throws TException { }

    @Override
    public String sayHi(String name) throws TException {
        return "Hi, " + name;
    }
}
