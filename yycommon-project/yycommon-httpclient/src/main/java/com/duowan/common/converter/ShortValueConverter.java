package com.duowan.common.converter;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 20:53
 */
public class ShortValueConverter extends AbstractConverter {

    public ShortValueConverter() {
        super(short.class);
    }

    @Override
    protected Object getObject(JSONObject jsonObject, String dataKey) {
        return jsonObject.getShortValue(dataKey);
    }
}
