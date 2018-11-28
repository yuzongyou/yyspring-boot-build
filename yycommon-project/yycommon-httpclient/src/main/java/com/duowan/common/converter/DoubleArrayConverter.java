package com.duowan.common.converter;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 20:53
 */
public class DoubleArrayConverter extends AbstractArrayConverter {

    public DoubleArrayConverter() {
        super(Double.class);
    }

    @Override
    protected <T> T[] getArrayObject(JSONObject jsonObject, String dataKey) {
        String content = jsonObject.getString(dataKey);
        if (null == content) {
            return null;
        }
        String[] strArr = content.split(",");
        Double[] arr = new Double[strArr.length];
        for (int i = 0; i < arr.length; ++i) {
            try {
                arr[i] = Double.parseDouble(strArr[i]);
            } catch (NumberFormatException e) {
                arr[i] = null;
            }
        }
        return (T[]) arr;
    }
}
