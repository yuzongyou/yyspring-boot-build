package com.duowan.common.thrift.server.exporter;

import com.duowan.common.thrift.server.exception.ThriftServiceExportException;
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
     * @param thriftServiceList  实例列表
     * @param applicationContext Spring 上下文
     * @param environment        环境变量
     * @throws ThriftServiceExportException 服务导出错误
     */
    void export(List<Object> thriftServiceList, ApplicationContext applicationContext, Environment environment) throws ThriftServiceExportException;


    /**
     * 终止任务
     *
     * @throws ThriftServiceExportException 终止异常
     */
    void stop() throws ThriftServiceExportException;

}
