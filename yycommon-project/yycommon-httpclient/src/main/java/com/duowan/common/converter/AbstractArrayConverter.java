package com.duowan.common.converter;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 20:54
 */
public abstract class AbstractArrayConverter implements ArrayConverter {

    private final Class<?> clazz;

    public AbstractArrayConverter(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean support(Class<?> clazz) {
        return this.clazz.equals(clazz);
    }

    @Override
    public <T> T[] convert(JSONObject jsonObject, String dataKey, Class<T> requireType) {
        return getArrayObject(jsonObject, dataKey);
    }

    protected abstract <T> T[] getArrayObject(JSONObject jsonObject, String dataKey);
}
