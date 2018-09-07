package com.duowan.common.dns.util;

/**
 * @author Arvin
 */
public abstract class AbstractDnsCacheWrapper implements DnsCacheInterceptor {

    @Override
    public void beforePut(String host, Object object) {

    }

    @Override
    public void afterPut(String host, Object object) {

    }

    @Override
    public void beforeGet(String host) {

    }

    @Override
    public void afterGet(String host, Object object) {

    }
}
