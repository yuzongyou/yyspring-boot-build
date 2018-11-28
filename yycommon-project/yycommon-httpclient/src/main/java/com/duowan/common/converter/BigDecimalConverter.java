package com.duowan.common.converter;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 20:53
 */
public class BigDecimalConverter extends AbstractConverter {

    public BigDecimalConverter() {
        super(BigDecimal.class);
    }

    @Override
    protected Object getObject(JSONObject jsonObject, String dataKey) {
        return jsonObject.getBigDecimal(dataKey);
    }
}
