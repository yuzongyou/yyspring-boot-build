package com.duowan.common.converter;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 21:00
 */
public class StringArrayConverter extends AbstractArrayConverter {

    private static final String[] EMPTY_ARRAY = new String[0];

    public StringArrayConverter() {
        super(String.class);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected <T> T[] getArrayObject(JSONObject jsonObject, String dataKey) {
        String content = jsonObject.getString(dataKey);
        if (null == content) {
            return (T[]) EMPTY_ARRAY;
        }
        return (T[]) content.split(",");
    }
}
