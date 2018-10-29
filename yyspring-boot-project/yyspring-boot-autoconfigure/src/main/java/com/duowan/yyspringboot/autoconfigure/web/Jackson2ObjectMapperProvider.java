package com.duowan.yyspringboot.autoconfigure.web;

import com.duowan.common.web.formatter.YySimpleDateFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/26 10:29
 */
public interface Jackson2ObjectMapperProvider {

    /**
     * 构造一个 ObjectMapper
     *
     * @return 返回ObjectMapper
     */
    default ObjectMapper provide() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new YySimpleDateFormat());
        // 序列化时，include规则为：始终包含为空字符串，空集合，空数组或null的属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // 忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
