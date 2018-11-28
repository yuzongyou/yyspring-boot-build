package com.duowan.common.converter;

import com.alibaba.fastjson.JSONObject;
import com.duowan.common.exception.HttpResponseConvertException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 20:58
 */
public class ConverterContext {

    private static List<Converter> oneObjectConverterList = new ArrayList<>();
    private static List<ArrayConverter> arrayObjectConverterList = new ArrayList<>();

    static {
        initOneObjectConverter();
        initArrayObjectConverter();
    }

    private static void initArrayObjectConverter() {
        arrayObjectConverterList.add(new StringArrayConverter());
        arrayObjectConverterList.add(new IntegerArrayConverter());
        arrayObjectConverterList.add(new LongArrayConverter());
        arrayObjectConverterList.add(new BooleanArrayConverter());
        arrayObjectConverterList.add(new FloatArrayConverter());
        arrayObjectConverterList.add(new DoubleArrayConverter());
        arrayObjectConverterList.add(new ShortArrayConverter());
    }

    private static void initOneObjectConverter() {
        oneObjectConverterList.add(new StringConverter());
        oneObjectConverterList.add(new IntegerValueConverter());
        oneObjectConverterList.add(new IntegerConverter());
        oneObjectConverterList.add(new LongValueConverter());
        oneObjectConverterList.add(new LongConverter());
        oneObjectConverterList.add(new BooleanValueConverter());
        oneObjectConverterList.add(new BooleanConverter());
        oneObjectConverterList.add(new FloatValueConverter());
        oneObjectConverterList.add(new FloatConverter());
        oneObjectConverterList.add(new DoubleValueConverter());
        oneObjectConverterList.add(new DoubleConverter());
        oneObjectConverterList.add(new DateConverter());
        oneObjectConverterList.add(new SqlDateConverter());
        oneObjectConverterList.add(new ShortValueConverter());
        oneObjectConverterList.add(new ShortConverter());
        oneObjectConverterList.add(new TimestampConverter());
        oneObjectConverterList.add(new BigIntegerConverter());
        oneObjectConverterList.add(new BigDecimalConverter());
    }

    public static <T> T convertOneObject(Class<T> requireType, JSONObject jsonObject, String dateKey) {

        for (Converter converter : oneObjectConverterList) {
            if (converter.support(requireType)) {
                return converter.convert(jsonObject, dateKey, requireType);
            }
        }

        return jsonObject.getObject(dateKey, requireType);
    }

    public static <T> T[] convertToArray(Class<T> requireType, JSONObject jsonObject, String dateKey) {
        for (ArrayConverter converter : arrayObjectConverterList) {
            if (converter.support(requireType)) {
                return converter.convert(jsonObject, dateKey, requireType);
            }
        }
        throw new HttpResponseConvertException("Array Convert failed: " + requireType);
    }
}
