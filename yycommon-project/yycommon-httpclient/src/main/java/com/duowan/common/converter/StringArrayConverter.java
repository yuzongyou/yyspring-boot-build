package com.duowan.common.converter;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 21:00
 */
public class StringArrayConverter extends AbstractArrayConverter {

    public StringArrayConverter() {
        super(String.class);
    }

    @Override
    protected <T> T[] getArrayObject(JSONObject jsonObject, String dataKey) {
        String content = jsonObject.getString(dataKey);
        if (null == content) {
            return null;
        }
        return (T[]) content.split(",");
    }
}
