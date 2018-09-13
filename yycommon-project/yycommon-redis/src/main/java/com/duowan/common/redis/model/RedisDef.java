package com.duowan.common.redis.model;

/**
 * Redis 定义
 *
 * @author Arvin
 */
public interface RedisDef {

    /**
     * 获取唯一标识符
     *
     * @return 返回唯一标识符
     */
    String getId();

    /**
     * 是否是默认的 Redis
     *
     * @return true 主， false-非主
     */
    boolean isPrimary();

    /**
     * 设置是否 primary
     *
     * @param primary 是否primary
     */
    void setPrimary(boolean primary);
}
