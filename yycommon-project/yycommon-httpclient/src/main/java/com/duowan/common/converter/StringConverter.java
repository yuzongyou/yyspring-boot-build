package com.duowan.common.converter;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 21:00
 */
public class StringConverter extends AbstractConverter {

    public StringConverter() {
        super(String.class);
    }

    @Override
    protected Object getObject(JSONObject jsonObject, String dataKey) {
        return jsonObject.getString(dataKey);
    }
}
