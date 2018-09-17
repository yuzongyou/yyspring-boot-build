package com.duowan.common.thrift.client.config;

/**
 * Thrift Server Node
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 17:11
 */
public class ThriftServerNode {

    /** 节点唯一标识 */
    private String id;

    /** 主机地址 */
    private String host;

    /** 服务端口 */
    private int port;

    /** 是否存活 */
    private boolean alive;

    public ThriftServerNode(String host, int port) {
        this.host = host;
        this.port = port;

        this.id = host + ":" + port;
    }

    public String getId() {

        if (null == id || "".equals(id.trim())) {
            id = host + ":" + port;
        }

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public String toString() {
        return "ThriftServerNode{" +
                "id='" + id + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", alive=" + alive +
                '}';
    }
}