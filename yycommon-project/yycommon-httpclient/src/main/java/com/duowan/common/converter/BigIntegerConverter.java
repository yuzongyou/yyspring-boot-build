package com.duowan.common.converter;

import com.alibaba.fastjson.JSONObject;

import java.math.BigInteger;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 20:53
 */
public class BigIntegerConverter extends AbstractConverter {

    public BigIntegerConverter() {
        super(BigInteger.class);
    }

    @Override
    protected Object getObject(JSONObject jsonObject, String dataKey) {
        return jsonObject.getBigInteger(dataKey);
    }
}
