package com.duowan.common.web.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/19 17:08
 */
public class YyDateConverter implements Converter<String, Date> {

    private static final List<DateConverter> DATE_CONVERTERS = loadDateConverterList();

    private static List<DateConverter> loadDateConverterList() {

        List<DateConverter> list = new ArrayList<>();
        int order = Ordered.HIGHEST_PRECEDENCE;
        list.add(new LongDateConverter(order++));
        list.add(new PatternDateConverter("yyyy-MM-dd HH:mm:ss", order++));
        list.add(new PatternDateConverter("yyyy/MM/dd HH:mm:ss", order++));
        list.add(new PatternDateConverter("yyyy-MM-dd", order++));
        list.add(new PatternDateConverter("yyyy/MM/dd", order++));
        list.add(new ConstructorDateConverter(order));

        return list;
    }

    @Override
    public Date convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }

        source = source.trim();


        for (DateConverter converter : DATE_CONVERTERS) {
            Date date = converter.convert(source);
            if (null != date) {
                return date;
            }
        }

        throw new IllegalArgumentException("无法将字符串[" + source + "] 转换成 java.uti.Date 对象");
    }
}
