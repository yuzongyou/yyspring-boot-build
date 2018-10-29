package com.duowan.common.web.formatter;

import com.duowan.common.web.converter.YyDateConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/19 17:21
 */
public class YySimpleDateFormat extends SimpleDateFormat {

    private YyDateConverter converter = new YyDateConverter();

    public YySimpleDateFormat() {
    }

    public YySimpleDateFormat(String pattern) {
        super(pattern);
    }

    @Override
    public Date parse(String source) throws ParseException {
        try {
            return super.parse(source);
        } catch (ParseException ignored) {
        }
        return converter.convert(source);
    }
}
