package com.duowan.common.thrift.client.monitor;

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
        /** 有效的 */
        UP,

        /** 节点挂掉 */
        DOWN
    }

    private Status status;

    private String message;

    public MonitorResult(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
