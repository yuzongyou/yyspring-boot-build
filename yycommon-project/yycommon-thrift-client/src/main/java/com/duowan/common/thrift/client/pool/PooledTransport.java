package com.duowan.common.thrift.client.pool;

import com.duowan.common.thrift.client.ClientType;
import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;
import com.duowan.common.thrift.client.factory.TClientFactory;
import com.duowan.common.thrift.client.factory.TProtocolFactory;
import com.duowan.common.thrift.client.monitor.ClientValidator;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 20:48
 */
public class PooledTransport {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Transport 对象
     */
    protected final TTransport transport;

    /**
     * 使用的服务节点
     */
    protected final ThriftServerNode serverNode;

    /**
     * 业务类名称
     */
    protected final TClientConfig clientConfig;

    /**
     * 是否是有效的连接
     */
    protected volatile boolean discard = false;

    /**
     * 最后一次访问时间
     */
    protected volatile long lastAccessTime;

    /**
     * 创建时间
     */
    protected final long createTime;

    /**
     * 备注信息，比如连接要丢弃的时候，错误信息
     */
    protected String remark;

    /**
     * 客户端实例
     **/
    private Map<ClientKey, Object> clientMap = new HashMap<>();

    public PooledTransport(ThriftServerNode serverNode, TClientConfig clientConfig, TTransport transport) throws Exception {
        this.transport = transport;
        this.serverNode = serverNode;
        this.clientConfig = clientConfig;
        this.createTime = System.currentTimeMillis();

        this.initClientMap();
    }

    private void initClientMap() throws Exception {

        List<TProtocolFactory> factoryList = clientConfig.getProtocolFactories();
        for (TProtocolFactory factory : factoryList) {
            String router = factory.router();
            Set<ClientType> clientTypes = factory.clientTypes();
            if (clientTypes == null || clientTypes.isEmpty()) {
                clientTypes = new HashSet<>(Collections.singletonList(ClientType.IFACE));
            }

            for (ClientType clientType : clientTypes) {
                ClientKey key = buildClientKey(router, clientType);
                Object client = TClientFactory.createClientByClientType(transport, clientType, factory, clientConfig);
                if (logger.isDebugEnabled()) {
                    logger.debug("Create thrift client {} from node {} and transport {}", key, serverNode, transport);
                }
                this.clientMap.put(key, client);
            }
        }
    }

    private ClientKey buildClientKey(String router, ClientType clientType) {
        return new ClientKey(router, clientType);
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

    public TClientConfig getClientConfig() {
        return clientConfig;
    }

    public Object getClient(String router, ClientType clientType) {
        return clientMap.get(buildClientKey(router, clientType));
    }

    @Override
    public String toString() {
        return "@" + Integer.toHexString(hashCode()) + ":alive=" + !isDiscard() + ":" + serverNode + ":remark=" + getRemark();
    }

    /**
     * 校验对象，用于自定义扩展校验方法
     *
     * @return 返回是否通过校验
     */
    public boolean validateObject() {
        ClientValidator validator = clientConfig.getClientValidator();
        if (null == validator) {
            return true;
        }
        for (Map.Entry<ClientKey, Object> entry : clientMap.entrySet()) {
            boolean isValid = validator.validateObject(this, entry.getKey(), entry.getValue());
            if (!isValid) {
                return false;
            }
        }
        return true;
    }
}
