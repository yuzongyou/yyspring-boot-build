package com.duowan.common.converter;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 20:54
 */
public abstract class AbstractConverter implements Converter {

    private final Class<?> clazz;

    public AbstractConverter(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean support(Class<?> clazz) {
        return this.clazz.equals(clazz);
    }

    @Override
    public <T> T convert(JSONObject jsonObject, String dataKey, Class<T> requireType) {
        return requireType.cast(getObject(jsonObject, dataKey));
    }

    protected abstract Object getObject(JSONObject jsonObject, String dataKey);
}
