package com.duowan.common.thrift.client.pool;

import com.duowan.common.thrift.client.config.ThriftServerNode;
import org.apache.thrift.transport.TTransport;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 20:48
 */
public class PooledTransport {

    /** Transport 对象 */
    protected final TTransport transport;

    /** 使用的服务节点 */
    protected final ThriftServerNode serverNode;

    /** 业务类名称 */
    protected final Class<?> serviceClass;

    /** 是否是有效的连接 */
    protected volatile boolean discard = false;

    /** 最后一次访问时间 */
    protected volatile long lastAccessTime;

    /** 创建时间 */
    protected final long createTime;

    /** 备注信息，比如连接要丢弃的时候，错误信息 */
    protected String remark;

    public PooledTransport(ThriftServerNode serverNode, Class<?> serviceClass, TTransport transport) {
        this.transport = transport;
        this.serverNode = serverNode;
        this.serviceClass = serviceClass;
        this.createTime = System.currentTimeMillis();
    }

    public TTransport getTransport() {
        return transport;
    }

    public ThriftServerNode getServerNode() {
        return serverNode;
    }

    public boolean isDiscard() {
        return discard || !serverNode.isAlive();
    }

    public void discard() {
        this.discard = true;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Class<?> getServiceClass() {
        return serviceClass;
    }

    @Override
    public String toString() {
        return "@" + Integer.toHexString(hashCode()) + ":alive=" + !isDiscard() + ":" + serverNode + ":serviceClass=" + getServiceClass() + ":remark=" + getRemark();
    }

}
