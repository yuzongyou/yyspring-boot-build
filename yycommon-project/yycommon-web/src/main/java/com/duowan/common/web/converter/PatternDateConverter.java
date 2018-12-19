package com.duowan.common.web.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/19 16:53
 */
public class PatternDateConverter implements DateConverter {

    private final String pattern;
    private final int order;

    public PatternDateConverter(String pattern, int order) {
        this.pattern = pattern;
        this.order = order;
    }

    @Override
    public Date convert(String source) {
        if (source.length() == pattern.length()) {
            return parseDate(pattern, source);
        }
        return null;
    }

    private Date parseDate(String pattern, String source) {
        try {
            return new SimpleDateFormat(pattern).parse(source);
        } catch (ParseException ignored) {
            return null;
        }
    }

    @Override
    public int getOrder() {
        return order;
    }
}
