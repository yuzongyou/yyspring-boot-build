package com.duowan.common.web.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/19 17:08
 */
public class YyDateConverter implements Converter<String, Date> {

    public static final List<String> DEFAULT_PATTERNS = Arrays.asList(
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy/MM/dd",
            "yyyyMMdd"
    );

    private List<String> patterns = DEFAULT_PATTERNS;

    @Override
    public Date convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }

        source = source.trim();

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
