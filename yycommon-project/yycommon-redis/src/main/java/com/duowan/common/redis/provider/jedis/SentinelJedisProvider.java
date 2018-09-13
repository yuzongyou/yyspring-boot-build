package com.duowan.common.redis.provider.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

/**
 * 支持哨兵模式的 Jedis 连接返回
 *
 * @author Arvin
 */
public class SentinelJedisProvider extends AbstractProvider {

    /**
     * 支持哨兵模式的连接池实现
     */
    protected JedisSentinelPool jedisSentinelPool;

    public JedisSentinelPool getJedisSentinelPool() {
        return jedisSentinelPool;
    }

    public void setJedisSentinelPool(JedisSentinelPool jedisSentinelPool) {
        this.jedisSentinelPool = jedisSentinelPool;
    }

    @Override
    public Jedis getResource() {
        return jedisSentinelPool.getResource();
    }
}
