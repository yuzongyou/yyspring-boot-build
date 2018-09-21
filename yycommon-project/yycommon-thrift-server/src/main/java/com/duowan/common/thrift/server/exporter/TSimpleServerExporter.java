package com.duowan.common.thrift.server.exporter;

import org.apache.thrift.server.TSimpleServer;

/**
 * 100% 不推荐使用这个类进行服务发布，单线程阻塞，很有问题
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 23:37
 */
public class TSimpleServerExporter extends AbstractThriftServiceExporter<TSimpleServer.Args> {

    public TSimpleServerExporter() {
        super();
    }

    public TSimpleServerExporter(int port) {
        super(port);
    }

    public TSimpleServerExporter(boolean joinToParentThread) {
        super(joinToParentThread);
    }

    public TSimpleServerExporter(int port, boolean joinToParentThread) {
        super(port, joinToParentThread);
    }
}
