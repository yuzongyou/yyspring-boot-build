package com.duowan.common.web.converter;

import java.util.Date;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/19 16:57
 */
public class ConstructorDateConverter implements DateConverter {

    private final int order;

    public ConstructorDateConverter(int order) {
        this.order = order;
    }

    @Override
    public Date convert(String source) {
        try {
            return new Date(source);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getOrder() {
        return order;
    }
}
