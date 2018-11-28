package com.duowan.common.converter;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 20:53
 */
public class DateConverter extends AbstractConverter {

    public DateConverter() {
        super(Date.class);
    }

    @Override
    protected Object getObject(JSONObject jsonObject, String dataKey) {
        return jsonObject.getDate(dataKey);
    }
}