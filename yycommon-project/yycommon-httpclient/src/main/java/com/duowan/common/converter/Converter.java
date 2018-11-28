package com.duowan.common.converter;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 20:51
 */
public interface Converter {

    boolean support(Class<?> clazz);

    <T> T convert(JSONObject jsonObject, String dataKey, Class<T> requireType);
}
