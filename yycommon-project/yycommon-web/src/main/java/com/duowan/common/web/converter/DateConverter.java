package com.duowan.common.web.converter;

import org.springframework.core.Ordered;

import java.util.Date;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/19 16:51
 */
public interface DateConverter extends Ordered {

    /**
     * 日期转换
     *
     * @param source 源字符串
     * @return null 表示不支持， 否则返会 Date 对象
     */
    Date convert(String source);
}
