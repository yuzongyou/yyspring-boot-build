package com.duowan.common.thrift.server.exporter;

import org.apache.thrift.server.TServer;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 14:30
 */
public class ThriftExportThread extends Thread {

    private final TServer server;

    /**
     * 用于停止线程
     **/
    private boolean alive = true;

    public ThriftExportThread(TServer server) {
        this.server = server;
        setName("ThriftExporter");
    }

    @Override
    public void run() {
        while (alive) {
            server.serve();
        }
    }

    public void shutdownNow() {
        try {
            this.alive = false;
            server.stop();
        } catch (Exception ignored) {
        }
    }
}
