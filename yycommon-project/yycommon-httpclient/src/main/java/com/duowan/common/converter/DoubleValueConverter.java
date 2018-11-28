package com.duowan.common.converter;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 20:53
 */
public class DoubleValueConverter extends AbstractConverter {

    public DoubleValueConverter() {
        super(double.class);
    }

    @Override
    protected Object getObject(JSONObject jsonObject, String dataKey) {
        return jsonObject.getDoubleValue(dataKey);
    }
}
