package com.duowan.common.thrift.server.exporter;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 22:05
 */
public interface ThriftServiceExporter {

    /**
     * 获取发布服务所使用的 TServer 对象
     *
     * @param thriftServiceWrappers 实例列表
     * @param applicationContext    Spring 上下文
     * @param environment           环境变量
     */
    void export(List<ThriftServiceWrapper> thriftServiceWrappers, ApplicationContext applicationContext, Environment environment);


    /**
     * 终止任务
     */
    void stop();

}
