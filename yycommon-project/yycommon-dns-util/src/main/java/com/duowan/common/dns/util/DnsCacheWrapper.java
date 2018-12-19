package com.duowan.common.dns.util;

import com.duowan.common.dns.exception.DnsException;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * @author Arvin
 */
public class DnsCacheWrapper extends LinkedHashMap<String, Object> {

    private final transient DnsCacheInterceptor interceptor;

    public DnsCacheWrapper(DnsCacheInterceptor interceptor) {
        if (null == interceptor) {
            throw new DnsException(DnsCacheInterceptor.class.getSimpleName() + " 不能为空！");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), interceptor);
    }
}
