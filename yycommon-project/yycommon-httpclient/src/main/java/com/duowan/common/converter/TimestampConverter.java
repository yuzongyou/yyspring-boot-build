package com.duowan.common.converter;

import com.alibaba.fastjson.JSONObject;

import java.sql.Timestamp;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 20:53
 */
public class TimestampConverter extends AbstractConverter {

    public TimestampConverter() {
        super(Timestamp.class);
    }

    @Override
    protected Object getObject(JSONObject jsonObject, String dataKey) {
        return jsonObject.getTimestamp(dataKey);
    }
}
