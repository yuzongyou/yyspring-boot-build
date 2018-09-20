package com.duowan.common.thrift.server.config;

import org.apache.thrift.server.TServer;

/**
 * TServer 参数配置
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 23:17
 */
public interface ArgsConfigurer<T extends TServer.AbstractServerArgs<T>> {

    /**
     * 参数对象
     *
     * @param args 参数对象
     */
    void configure(T args);
}
