package com.duowan.common.converter;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 20:53
 */
public class ShortArrayConverter extends AbstractArrayConverter {

    public ShortArrayConverter() {
        super(Short.class);
    }

    @Override
    protected <T> T[] getArrayObject(JSONObject jsonObject, String dataKey) {
        String content = jsonObject.getString(dataKey);
        if (null == content) {
            return null;
        }
        String[] strArr = content.split(",");
        Short[] arr = new Short[strArr.length];
        for (int i = 0; i < arr.length; ++i) {
            try {
                arr[i] = Short.parseShort(strArr[i]);
            } catch (NumberFormatException e) {
                arr[i] = null;
            }
        }
        return (T[]) arr;
    }
}
