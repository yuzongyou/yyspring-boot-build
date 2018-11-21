package com.duowan.common.httpclient;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/21 15:02
 */
public interface PreRequestInterceptor<T extends AbstractHcRequestContext> {

    void preHandle(T context);
}
