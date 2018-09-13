package com.duowan.common.redis.provider.def;

import com.duowan.common.redis.model.RedisDef;

/**
 * @author Arvin
 * @since 2018/5/23 9:43
 */
public interface RedisDefProvider {

    /**
     * 是否支持处理该RedisDef
     *
     * @param redisDef redis定义
     * @return 返回是否支持处理
     */
    boolean support(RedisDef redisDef);

    /**
     * 检查配置同时应用默认配置
     *
     * @param redisDef redis定义
     * @return true 表示成功处理了，false标识没有成功处理
     */
    boolean checkAndApplyDefaultConfig(RedisDef redisDef);


}
