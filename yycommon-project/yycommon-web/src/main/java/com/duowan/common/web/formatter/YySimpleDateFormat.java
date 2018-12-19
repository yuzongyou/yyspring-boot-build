package com.duowan.common.web.formatter;

import com.duowan.common.web.converter.YyDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/19 17:21
 */
public class YySimpleDateFormat extends SimpleDateFormat {

    private static final Logger LOGGER = LoggerFactory.getLogger(YySimpleDateFormat.class);

    private transient YyDateConverter converter = new YyDateConverter();

    public YySimpleDateFormat() {
    }

    public YySimpleDateFormat(String pattern) {
        super(pattern);
    }

    @Override
    public Date parse(String source) {
        try {
            return super.parse(source);
        } catch (ParseException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(e.getMessage(), e);
            }
        }
        return converter.convert(source);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        YySimpleDateFormat that = (YySimpleDateFormat) o;
        return Objects.equals(converter, that.converter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), converter);
    }
}
