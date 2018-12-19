package com.duowan.common.web.converter;

import java.util.Date;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/19 16:52
 */
public class LongDateConverter implements DateConverter {

    private final int order;

    public LongDateConverter(int order) {
        this.order = order;
    }

    @Override
    public Date convert(String source) {
        if (source.matches("^[0-9]+$")) {
            return new Date(Long.parseLong(source));
        }
        return null;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
