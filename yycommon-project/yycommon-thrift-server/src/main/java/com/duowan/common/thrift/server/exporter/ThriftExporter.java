package com.duowan.common.thrift.server.exporter;

/**
 * Thrift 服务发布接口，发布Thrift服务的话需要实现这个接口
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 17:28
 */
public interface ThriftExporter {

    /**
     * 导出服务
     */
    void export();
}
