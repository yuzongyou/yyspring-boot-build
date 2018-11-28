package com.duowan.common.converter;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 20:53
 */
public class BooleanArrayConverter extends AbstractArrayConverter {

    public BooleanArrayConverter() {
        super(Boolean.class);
    }

    @Override
    protected <T> T[] getArrayObject(JSONObject jsonObject, String dataKey) {
        String content = jsonObject.getString(dataKey);
        if (null == content) {
            return null;
        }
        String[] strArr = content.split(",");
        Boolean[] arr = new Boolean[strArr.length];
        for (int i = 0; i < arr.length; ++i) {
            try {
                arr[i] = Boolean.parseBoolean(strArr[i]);
            } catch (NumberFormatException e) {
                arr[i] = null;
            }
        }
        return (T[]) arr;
    }
}
