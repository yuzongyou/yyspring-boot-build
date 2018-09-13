package com.duowan.common.redis;

import redis.clients.jedis.Pipeline;

/**
 * @author Arvin
 */
public interface PipelineExecutor<T> {

    /**
     * 使用管道命令执行
     *
     * @param pipeline 管道命令
     * @return 返回指定结果
     */
    T execute(Pipeline pipeline);
}
