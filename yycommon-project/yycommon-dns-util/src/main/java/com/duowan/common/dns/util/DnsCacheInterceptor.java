package com.duowan.common.dns.util;

/**
 * DNS 缓存拦截器
 *
 * @author Arvin
 */
public interface DnsCacheInterceptor {

    /**
     * 添加缓存之前, 不允许抛出异常
     *
     * @param host   主机地址
     * @param object 缓存对象
     */
    void beforePut(String host, Object object);

    /**
     * 添加缓存之后, 不允许抛出异常
     *
     * @param host   主机地址
     * @param object 缓存对象
     */
    void afterPut(String host, Object object);

    /**
     * 获取缓存之前, 不允许抛出异常
     *
     * @param host 主机地址
     */
    void beforeGet(String host);

    /**
     * 获取缓存之后, 不允许抛出异常
     *
     * @param host   主机地址
     * @param object 获取到的缓存对象
     */
    void afterGet(String host, Object object);
}
