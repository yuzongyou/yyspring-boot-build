package com.duowan.common.jdbc;

import com.duowan.common.jdbc.exception.JdbcException;
import com.duowan.common.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author Arvin
 * @since 2018/5/21 20:59
 */
public abstract class JdbcHelper {

    /**
     * 生成UUID并去掉横杠，转化为大写
     *
     * @return 返回UUID
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }


    public static boolean isOverZero(String numberString) {
        if (null == numberString) {
            return false;
        }

        try {
            return Long.valueOf(String.valueOf(numberString)) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidPrimaryKeyValue(Object value) {
        if (null == value) {
            return false;
        }
        if (value instanceof CharSequence) {
            return StringUtils.isNotBlank(String.valueOf(value));
        }
        return isOverZero(String.valueOf(value));
    }

    /**
     * 创建当前时间，根据指定的时间类型
     *
     * @param timeType 时间类型
     * @return 返回时间对象
     */
    public static Object createCurrentTimeByTimeType(Class<?> timeType) {
        AssertUtil.assertTrue(isTimeType(timeType), "不支持的时间类型，无法生成！");
        long current = System.currentTimeMillis();
        if (timeType == Date.class) {
            return new Date();
        }
        if (timeType == java.sql.Date.class) {
            return new java.sql.Date(current);
        }
        if (timeType == java.sql.Timestamp.class) {
            return new java.sql.Timestamp(current);
        }
        if (timeType == java.sql.Time.class) {
            return new java.sql.Time(current);
        }

        throw new JdbcException("未知的时间类型，无法生成！");
    }

    @SuppressWarnings({"unchecked"})
    public static boolean isTimeType(Class<?> timeType) {
        if (null == timeType) {
            return false;
        }
        Set<Class<?>> supportTypes = new HashSet<Class<?>>(Arrays.asList(
                Date.class,
                java.sql.Date.class,
                java.sql.Timestamp.class,
                java.sql.Time.class
        ));
        return supportTypes.contains(timeType);
    }

    @SuppressWarnings({"unchecked"})
    public static boolean isStringType(Class<?> type) {
        if (null == type) {
            return false;
        }
        Set<Class<?>> supportTypes = new HashSet<Class<?>>(Arrays.asList(
                String.class, CharSequence.class
        ));
        return supportTypes.contains(type);
    }

    @SuppressWarnings({"unchecked"})
    public static boolean isIntType(Class<?> type) {
        if (null == type) {
            return false;
        }
        return int.class == type || Integer.class == type;
    }

    @SuppressWarnings({"unchecked"})
    public static boolean isLongType(Class<?> type) {
        if (null == type) {
            return false;
        }
        return long.class == type || Long.class == type;
    }

    @SuppressWarnings({"unchecked"})
    public static boolean isBooleanType(Class<?> type) {
        if (null == type) {
            return false;
        }
        return boolean.class == type || Boolean.class == type;
    }


    /**
     * 是否是原始类型
     *
     * @param type 类类型
     * @return 是否给定的类型的原始类型对象
     */
    @SuppressWarnings({"unchecked"})
    public static boolean isPrimitiveType(Class<?> type) {
        if (null == type) {
            return false;
        }
        Set<Class<?>> primitiveTypes = new HashSet<Class<?>>(Arrays.asList(
                int.class, long.class, char.class, short.class,
                byte.class, boolean.class, float.class, double.class
        ));

        return primitiveTypes.contains(type);
    }

    /**
     * 是否是数字类型
     *
     * @param type 类类型
     * @return 是否数字
     */
    @SuppressWarnings({"unchecked"})
    public static boolean isNumberType(Class<?> type) {
        if (null == type) {
            return false;
        }
        Set<Class<?>> numberTypes = new HashSet<Class<?>>(Arrays.asList(
                int.class, long.class, short.class,
                float.class, double.class,
                Integer.class, Long.class, Number.class, Double.class,
                Float.class, Short.class, Byte.class, BigDecimal.class,
                BigInteger.class
        ));

        return numberTypes.contains(type);
    }

    /**
     * 获取指定的classType的默认值
     *
     * @param classType 类
     * @return 原始类型按照原始类型默认值，包装类型则为null
     */
    public static Object getTypeDefaultValue(Class<?> classType) {
        AssertUtil.assertNotNull(classType, "要计算的类类型不能为null");

        if (classType == int.class) {
            return 0;
        }
        if (classType == long.class) {
            return 0L;
        }
        if (classType == char.class) {
            return 0;
        }
        if (classType == boolean.class) {
            return false;
        }
        if (classType == float.class) {
            return (float) 0;
        }
        if (classType == double.class) {
            return (double) 0;
        }
        if (classType == byte.class) {
            return (byte) 0;
        }
        if (classType == short.class) {
            return (short) 0;
        }

        return null;
    }

    /**
     * 获取原始类型对应的包装类型
     *
     * @param primitiveType 原始类型
     * @return 返回给定类型的包装类
     */
    public static Class<?> getPrimitiveWrapType(Class<?> primitiveType) {
        AssertUtil.assertNotNull(primitiveType, "要计算包装类型的原始类型不能为null");

        if (primitiveType == int.class) {
            return Integer.class;
        }
        if (primitiveType == long.class) {
            return Long.class;
        }
        if (primitiveType == char.class) {
            return Character.class;
        }
        if (primitiveType == boolean.class) {
            return Boolean.class;
        }
        if (primitiveType == float.class) {
            return Float.class;
        }
        if (primitiveType == double.class) {
            return Double.class;
        }
        if (primitiveType == byte.class) {
            return Byte.class;
        }
        if (primitiveType == short.class) {
            return Short.class;
        }

        return primitiveType;
    }

    /**
     * 类型是否匹配
     *
     * @param firstType  期望的类型
     * @param secondType 实际类型
     * @return 是否匹配
     */
    public static boolean isTypeMatch(Class<?> firstType, Class<?> secondType) {
        if (null == firstType || null == secondType) {
            return false;
        }
        Class<?> wrapFirstType = getPrimitiveWrapType(firstType);
        Class<?> wrapSecondType = getPrimitiveWrapType(secondType);

        if (wrapFirstType == wrapSecondType) {
            return true;
        }

        if (isTimeType(wrapFirstType) && isTimeType(wrapSecondType)) {
            return true;
        }

        return false;

    }

    /**
     * 转成 java.util.Date 对象
     *
     * @param value 时间值
     * @return 转换后的 java util Date 对象
     */
    public static Date convertToJavaUtilDate(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Date) {
            if (value.getClass() != java.util.Date.class) {
                return new java.util.Date(((Date) value).getTime());
            }
            return (java.util.Date) value;
        }

        return null;
    }

    /**
     * 符合条件的查询类
     */
    private static String[] ACCEPT_QUERY_CONDITION_SUFFIXES = new String[]{
            "Query", "Dto", "DTO", "Vo", "VO", "Condition", "Bean"
    };

    public static boolean isValidQueryCondition(Object queryCondition) {
        if (null == queryCondition) {
            return true;
        }
        return isValidQueryConditionType(queryCondition.getClass());
    }

    public static boolean isValidQueryConditionType(Class<?> queryConditionType) {
        if (null == queryConditionType) {
            return false;
        }
        String className = queryConditionType.getSimpleName();
        for (String validSuffix : ACCEPT_QUERY_CONDITION_SUFFIXES) {
            if (className.endsWith(validSuffix)) {
                return true;
            }
        }
        return false;
    }

    public static void checkQueryCondition(Object queryCondition) {
        AssertUtil.assertTrue(isValidQueryCondition(queryCondition),
                "不符合规定的查询条件，查询条件对象类名必须是以下后缀结尾：" + validQueryConditionSuffixesToString());
    }

    public static void checkQueryConditionType(Class<?> queryConditionType) {
        AssertUtil.assertTrue(isValidQueryConditionType(queryConditionType),
                "不符合规定的查询条件类型，查询条件类名必须是以下后缀结尾：" + validQueryConditionSuffixesToString());
    }

    public static String validQueryConditionSuffixesToString() {
        StringBuilder builder = new StringBuilder("[");
        for (String validSuffix : ACCEPT_QUERY_CONDITION_SUFFIXES) {
            builder.append(validSuffix).append(", ");
        }
        builder.setLength(builder.length() - 2);
        builder.append("]");
        return builder.toString();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return null != collection && !collection.isEmpty();
    }

    public static boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isValidPagingParams(int pageNo, int pageSize) {
        return pageNo > 0 && pageSize > 0;
    }

    public static boolean isMapInstance(Object object) {
        return null != object && (object instanceof Map);
    }

    private static Set<Class<?>> ALLOW_AUTO_INCREMENT_TYPES = null;

    public static Set<Class<?>> getAllowAutoIncrementTypes() {
        if (null == ALLOW_AUTO_INCREMENT_TYPES) {
            ALLOW_AUTO_INCREMENT_TYPES = new HashSet<>();
            ALLOW_AUTO_INCREMENT_TYPES.add(int.class);
            ALLOW_AUTO_INCREMENT_TYPES.add(Integer.class);
            ALLOW_AUTO_INCREMENT_TYPES.add(long.class);
            ALLOW_AUTO_INCREMENT_TYPES.add(Long.class);
        }
        return ALLOW_AUTO_INCREMENT_TYPES;
    }

    /**
     * 是否允许自增的类型
     *
     * @param type 类型
     * @return 返回是否允许自增
     */
    public static boolean isAllowAutoIncrementType(Class<?> type) {
        return getAllowAutoIncrementTypes().contains(type);
    }

    public static boolean isAllowUuidType(Class<?> type) {
        return null != type && (type == CharSequence.class || type == String.class);
    }

    public static String getAllowAutoIncrementTypeString() {
        Set<Class<?>> types = getAllowAutoIncrementTypes();
        StringBuilder builder = new StringBuilder();
        for (Class<?> clazz : types) {
            builder.append(clazz.getSimpleName()).append(",");
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    public static boolean isNumberPkValueOverZero(Object pkValue) {

        if (null == pkValue) {
            return false;
        }

        try {
            return Long.valueOf(String.valueOf(pkValue)) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidUuid(Object pkValue) {
        if (null == pkValue) {
            return false;
        }

        return StringUtils.isNotBlank(String.valueOf(pkValue));
    }

    /**
     * 给表达式前后加上括号对
     *
     * @param express 表达式
     * @return 返回添加括号对后的字符串
     */
    public static String wrapBracketPair(String express) {
        return express.matches("^ *\\(.*\\) *$") ? express.trim() : "(" + express.trim().replaceFirst("^ *\\(", "").replaceFirst("\\) *$", "") + ")";
    }
}
