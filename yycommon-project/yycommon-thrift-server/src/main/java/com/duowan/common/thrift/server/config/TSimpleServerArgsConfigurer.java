package com.duowan.common.thrift.server.config;

import org.apache.thrift.server.TSimpleServer;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 23:23
 */
public abstract class TSimpleServerArgsConfigurer implements ArgsConfigurer<TSimpleServer.Args> {

    @Override
    public final void configure(TSimpleServer.Args args) {

    }
}
