package com.duowan.common.dns.util;

import java.util.LinkedHashMap;

/**
 * @author Arvin
 */
public class DnsCacheWrapper extends LinkedHashMap<String, Object> {

    private final DnsCacheInterceptor interceptor;

    public DnsCacheWrapper(DnsCacheInterceptor interceptor) {
        if (null == interceptor) {
            throw new RuntimeException(DnsCacheInterceptor.class.getSimpleName() + " 不能为空！");
        }
        this.interceptor = interceptor;
    }

    @Override
    public Object put(String key, Object value) {
        this.interceptor.beforePut(key, value);
        Object ret = super.put(key, value);
        this.interceptor.afterPut(key, value);
        return ret;
    }

    @Override
    public Object get(Object key) {
        String stringKey = String.valueOf(key);
        this.interceptor.beforeGet(stringKey);
        Object ret = super.get(key);
        this.interceptor.afterGet(stringKey, ret);
        return ret;
    }
}
