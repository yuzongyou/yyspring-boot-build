package com.duowan.common.web.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/19 17:08
 */
public class YyDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }

        source = source.trim();

        if (source.matches("^[0-9]+$")) {
            return new Date(Long.parseLong(source));
        }

        Date date = null;
        String pattern;
        {
            pattern = "yyyy-MM-dd HH:mm:ss";
            if (source.length() == pattern.length() && source.contains("-")) {
                date = parseDate(pattern, source);
            }
            if (null != date) {
                return date;
            }
        }
        {
            pattern = "yyyy/MM/dd HH:mm:ss";
            if (source.length() == pattern.length() && source.contains("/")) {
                date = parseDate(pattern, source);
            }
            if (null != date) {
                return date;
            }
        }

        {
            pattern = "yyyy-MM-dd";
            if (source.length() == pattern.length() && source.contains("-")) {
                date = parseDate(pattern, source);
            }
            if (null != date) {
                return date;
            }
        }

        {
            pattern = "yyyy/MM/dd";
            if (source.length() == pattern.length() && source.contains("/")) {
                date = parseDate(pattern, source);
            }
            if (null != date) {
                return date;
            }
        }
        throw new IllegalArgumentException("无法将字符串[" + source + "] 转换成 java.uti.Date 对象");
    }

    private Date parseDate(String pattern, String source) {
        try {
            return new SimpleDateFormat(pattern).parse(source);
        } catch (ParseException ignored) {
            return null;
        }
    }
}
