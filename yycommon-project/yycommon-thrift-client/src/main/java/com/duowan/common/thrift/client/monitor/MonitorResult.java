package com.duowan.common.thrift.client.monitor;

import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;

/**
 * 监控结果
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 18:56
 */
public class MonitorResult {

    /**
     * 节点状态
     */
    public enum Status {
        /**
         * 有效的
         */
        UP,

        /**
         * 节点挂掉
         */
        DOWN
    }

    private final TClientConfig clientConfig;

    private final ThriftServerNode serverNode;

    private Status status;

    private String message;

    private Object other;

    public MonitorResult(TClientConfig clientConfig, ThriftServerNode serverNode, Status status, String message) {
        this.clientConfig = clientConfig;
        this.serverNode = serverNode;
        this.status = status;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getOther() {
        return other;
    }

    public TClientConfig getClientConfig() {
        return clientConfig;
    }

    public ThriftServerNode getServerNode() {
        return serverNode;
    }

    public void setOther(Object other) {
        this.other = other;
    }

    public boolean isUp() {
        return Status.UP.equals(status);
    }

    public boolean isDown() {
        return Status.DOWN.equals(status);
    }

    @Override
    public String toString() {
        return "MonitorResult{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", serverNode=" + serverNode +
                ", clientConfig=" + clientConfig +
                ", other=" + other +
                '}';
    }
}
