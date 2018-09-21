package com.duowan.common.thrift.server.exporter;

import org.apache.thrift.server.TNonblockingServer;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 14:56
 */
public class TNonblockingServerExporter extends AbstractThriftServiceExporter<TNonblockingServer.Args> {

    public TNonblockingServerExporter() {
        super();
    }

    public TNonblockingServerExporter(int port) {
        super(port);
    }

    public TNonblockingServerExporter(boolean joinToParentThread) {
        super(joinToParentThread);
    }

    public TNonblockingServerExporter(int port, boolean joinToParentThread) {
        super(port, joinToParentThread);
    }
}
