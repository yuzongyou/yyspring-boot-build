package com.duowan.common.thrift.client.pool;

import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 21:37
 */
public class TClientPoolConfig extends GenericKeyedObjectPoolConfig {

    public TClientPoolConfig() {
        this.setTestWhileIdle(true);
        // 连接空闲时间， 当设置了testWhileIdle的时候，连接空闲了以下时间就会去进行连接检查
        this.setTimeBetweenEvictionRunsMillis(30000L);

        this.setNumTestsPerEvictionRun(-1);
        this.setMaxTotal(100);
        this.setMaxIdlePerKey(100);
    }
}
