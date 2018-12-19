package com.duowan.common.web.converter;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/19 17:37
 */
public class YyDateConverterTest {

    @Test
    public void convert() {

        YyDateConverter converter = new YyDateConverter();

        assertNotNull(converter.convert("2018-10-10 10:10:01"));
        assertNotNull(converter.convert("2018/10/10 10:10:01"));
        assertNotNull(converter.convert("2018-10-10"));
        assertNotNull(converter.convert("2018/10/10"));
        assertNotNull(converter.convert("Wed Dec 19 2018 17:39:06 GMT+0800 (中国标准时间)"));
        assertNotNull(converter.convert(String.valueOf(System.currentTimeMillis())));

    }
}