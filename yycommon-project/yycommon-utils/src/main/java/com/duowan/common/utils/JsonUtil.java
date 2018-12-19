package com.duowan.common.utils;

import com.duowan.common.utils.exception.JsonException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Json 工具类
 *
 * @author Arvin
 */
public abstract class JsonUtil {

    private JsonUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * 默认时间格式
     */
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认 ObjectMapper
     */
    private static ObjectMapper defaultObjectMapper = buildDefaultObjectMapper();

    /**
     * 忽略未知属性
     */
    private static ObjectMapper ignoreUnknownFieldObjectMapper = buildIgnoreUnknownFieldObjectMapper();

    /**
     * 漂亮的输出
     */
    private static ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

    /**
     * 构造忽略未知属性的ObjectMapper
     */
    private static ObjectMapper buildIgnoreUnknownFieldObjectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }

    /**
     * 不用进行同步
     */
    private static Map<String, ObjectMapper> dateFormatMapperMap = new HashMap<>();

    private static ObjectMapper buildDefaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 默认忽略未知属性字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

        return objectMapper;
    }

    private static void handleException(Exception e, boolean throwException) {
        if (null == e) {
            return;
        }
        logger.warn("Json Handler error: " + e.getMessage(), e);
        if (throwException) {
            throw new JsonException(e);
        }
    }

    /**
     * 输出漂亮格式的 JSON
     *
     * @param object 对象
     * @return 返回漂亮 JSON格式对象
     */
    public static String toPrettyJson(Object object) {
        return toPrettyJson(object, true);
    }

    /**
     * 输出漂亮格式的 JSON
     *
     * @param object         对象
     * @param throwException 是否抛出异常
     * @return 返回漂亮 JSON格式对象
     */
    public static String toPrettyJson(Object object, boolean throwException) {

        if (null == object) {
            return null;
        }

        try {
            return writer.writeValueAsString(object);
        } catch (Exception e) {
            handleException(e, throwException);
        }
        return null;
    }

    public static String toJson(Object object) {
        return toJson(object, defaultObjectMapper);
    }

    public static String toJson(Object object, boolean throwException) {
        return toJson(object, defaultObjectMapper, throwException);
    }

    public static String toJson(Object object, ObjectMapper objectMapper) {
        return toJson(object, objectMapper, true);
    }

    public static String toJson(Object object, ObjectMapper objectMapper, boolean throwException) {
        if (null == objectMapper) {
            objectMapper = defaultObjectMapper;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            handleException(e, throwException);
        }
        return null;
    }

    /**
     * 将时间默认格式化为： yyyy-MM-dd HH:mm:ss
     *
     * @param object 要json化的对象
     * @return 返回 json 字符串
     */
    public static String toJsonWithDataFormat(Object object) {
        return toJson(object, getDateFormatObjectMapper(null));
    }

    public static String toJsonWithDataFormat(Object object, boolean throwException) {
        return toJson(object, getDateFormatObjectMapper(null), throwException);
    }

    /**
     * 将时间默认格式化为： yyyy-MM-dd HH:mm:ss
     *
     * @param object     要json化的对象
     * @param dateFormat 日期时间格式， 如 yyyy-MM-dd HH:mm:ss, yyyyMMdd, HH:mm:ss......
     * @return 返回 json 字符串
     */
    public static String toJsonWithDataFormat(Object object, String dateFormat) {
        return toJson(object, getDateFormatObjectMapper(dateFormat));
    }

    public static String toJsonWithDataFormat(Object object, String dateFormat, boolean throwException) {
        return toJson(object, getDateFormatObjectMapper(dateFormat), throwException);
    }

    /**
     * 获取指定的时间格式 ObjectMapper 如果为空的话抛出异常(PS: 该方法不需要同步，就算多创建几个对象也是没有关系)
     *
     * @param dateFormat 时间格式
     * @return 返回ObjectMapper
     */
    protected static ObjectMapper getDateFormatObjectMapper(String dateFormat) {

        String pattern = StringUtils.isBlank(dateFormat) ? DEFAULT_DATE_FORMAT : dateFormat;

        ObjectMapper objectMapper = dateFormatMapperMap.get(pattern.trim());
        if (null != objectMapper) {
            return objectMapper;
        }

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(simpleDateFormat);

            dateFormatMapperMap.put(pattern.trim(), objectMapper);

            return objectMapper;
        } catch (Exception e) {
            String message = "非法的时间格式[" + pattern + "], error=" + e.getMessage();
            logger.warn(message, e);
            throw new JsonException(message, e);
        }
    }

    /**
     * Json to 对象， 默认会忽略未知属性（即JSON中有但是实体没有的话将不会抛出异常）
     *
     * @param json               对象JSON字符串
     * @param valueType          要求的类型
     * @param ignoreUnknownField 是否忽略未知属性
     * @param <T>                结果类型
     * @return 返回指定类型的对象
     */
    public static <T> T toObject(String json, Class<T> valueType, boolean ignoreUnknownField) {

        return toObject(json, valueType, ignoreUnknownField, true);
    }

    public static <T> T toObject(String json, Class<T> valueType, boolean ignoreUnknownField, boolean throwException) {

        ObjectMapper objectMapper = ignoreUnknownField ? ignoreUnknownFieldObjectMapper : defaultObjectMapper;

        try {
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            handleException(e, throwException);
        }
        return null;
    }

    /**
     * Json to List， 默认会忽略未知属性（即JSON中有但是实体没有的话将不会抛出异常）
     *
     * @param jsonArray          对象JSON字符串
     * @param valueType          要求的类型
     * @param ignoreUnknownField 是否忽略未知属性
     * @param <T>                结果类型
     * @return 返回对象列表
     */
    public static <T> List<T> toObjectList(String jsonArray, Class<T> valueType, boolean ignoreUnknownField) {
        return toObjectList(jsonArray, valueType, ignoreUnknownField, true);
    }

    public static <T> List<T> toObjectList(String jsonArray, Class<T> valueType, boolean ignoreUnknownField, boolean throwException) {
        if (StringUtils.isBlank(jsonArray)) {
            return new ArrayList<>();
        }

        CollectionType listValueType = TypeFactory.defaultInstance().constructCollectionType(List.class, valueType);

        ObjectMapper objectMapper = ignoreUnknownField ? ignoreUnknownFieldObjectMapper : defaultObjectMapper;

        try {
            return objectMapper.readValue(jsonArray, listValueType);
        } catch (Exception e) {
            handleException(e, throwException);
        }
        return new ArrayList<>(0);
    }

    /**
     * Json to List， 默认会忽略未知属性（即JSON中有但是实体没有的话将不会抛出异常）
     *
     * @param jsonArray 对象JSON字符串
     * @param valueType 要求的类型
     * @param <T>       结果类型
     * @return 返回对象列表
     */
    public static <T> List<T> toObjectList(String jsonArray, Class<T> valueType) {
        return toObjectList(jsonArray, valueType, true, true);
    }

    /**
     * Json to Set， 默认会忽略未知属性（即JSON中有但是实体没有的话将不会抛出异常）
     *
     * @param jsonArray          对象JSON字符串
     * @param valueType          要求的类型
     * @param ignoreUnknownField 是否忽略未知属性
     * @param <T>                结果类型
     * @return 返回对象集合
     */
    public static <T> Set<T> toObjectSet(String jsonArray, Class<T> valueType, boolean ignoreUnknownField) {
        return toObjectSet(jsonArray, valueType, ignoreUnknownField, true);
    }

    public static <T> Set<T> toObjectSet(String jsonArray, Class<T> valueType, boolean ignoreUnknownField, boolean throwException) {
        if (StringUtils.isBlank(jsonArray)) {
            return new HashSet<>();
        }

        CollectionType collectionValueType = TypeFactory.defaultInstance().constructCollectionType(Set.class, valueType);

        ObjectMapper objectMapper = ignoreUnknownField ? ignoreUnknownFieldObjectMapper : defaultObjectMapper;

        try {
            return objectMapper.readValue(jsonArray, collectionValueType);
        } catch (Exception e) {
            handleException(e, throwException);
        }
        return new HashSet<>(0);
    }

    /**
     * Json to Set， 默认会忽略未知属性（即JSON中有但是实体没有的话将不会抛出异常）
     *
     * @param jsonArray 对象JSON字符串
     * @param valueType 要求的类型
     * @param <T>       结果类型
     * @return 返回对象集合
     */
    public static <T> Set<T> toObjectSet(String jsonArray, Class<T> valueType) {
        return toObjectSet(jsonArray, valueType, true);
    }

    /**
     * Json to 对象Set，， 默认会忽略未知属性（即JSON中有但是实体没有的话将不会抛出异常）
     *
     * @param json      对象JSON字符串
     * @param valueType 要求的类型
     * @param <T>       结果类型
     * @return 返回对象
     */
    public static <T> T toObject(String json, Class<T> valueType) {
        return toObject(json, valueType, true);
    }

    /**
     * 转换成 Map
     *
     * @param json               json 字符串
     * @param keyType            key 类型
     * @param valueType          值类型
     * @param ignoreUnknownField 忽略未知属性
     * @param <K>                KEY类型
     * @param <V>                VALUE类型
     * @return 返回MAP
     */
    public static <K, V> Map<K, V> toObjectMap(String json, Class<K> keyType, Class<V> valueType, boolean ignoreUnknownField) {

        return toObjectMap(json, keyType, valueType, ignoreUnknownField, true);

    }

    public static <K, V> Map<K, V> toObjectMap(String json, Class<K> keyType, Class<V> valueType, boolean ignoreUnknownField, boolean throwException) {

        if (StringUtils.isBlank(json)) {
            return new HashMap<>();
        }

        MapType mapType = TypeFactory.defaultInstance().constructMapType(Map.class, keyType, valueType);

        ObjectMapper objectMapper = ignoreUnknownField ? ignoreUnknownFieldObjectMapper : defaultObjectMapper;

        try {
            return objectMapper.readValue(json, mapType);
        } catch (Exception e) {
            handleException(e, throwException);
        }

        return null;
    }

    /**
     * 转换成 Map
     *
     * @param json      json 字符串
     * @param keyType   key 类型
     * @param valueType 值类型
     * @param <K>       KEY类型
     * @param <V>       VALUE类型
     * @return 返回MAP
     */
    public static <K, V> Map<K, V> toObjectMap(String json, Class<K> keyType, Class<V> valueType) {
        return toObjectMap(json, keyType, valueType, true);
    }

    /**
     * 转换成 Map
     *
     * @param json json 字符串
     * @return 返回MAP
     */
    public static Map<String, Object> toObjectMap(String json) {
        return toObjectMap(json, String.class, Object.class, true);
    }

    /**
     * 转换成 Map
     *
     * @param json      json 字符串
     * @param valueType 值类型
     * @param <T>       值类型
     * @return 返回对象MAP
     */
    public static <T> Map<String, T> toObjectMap(String json, Class<T> valueType) {
        return toObjectMap(json, String.class, valueType, true);
    }

    /**
     * 转换成 JSON Node
     *
     * @param json json 字符串
     * @return 返回JsonNode
     */
    public static JsonNode toJsonNode(String json) {
        return toJsonNode(json, true);
    }

    public static JsonNode toJsonNode(String json, boolean throwException) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return defaultObjectMapper.readTree(json);
        } catch (Exception e) {
            handleException(e, throwException);
        }
        return null;
    }

    /**
     * 将 DataMap 指定属性转换成 Integer 类型
     *
     * @param dataMap      数据MAP
     * @param fieldName    属性名称
     * @param defaultValue 默认值
     * @return 返回Integer
     */
    public static Integer getInteger(Map<String, Object> dataMap, String fieldName, Integer defaultValue) {
        return ConvertUtil.toInteger(getObject(dataMap, fieldName), defaultValue);
    }

    /**
     * 将 DataMap 指定属性转换成 int 类型
     *
     * @param dataMap   数据MAP
     * @param fieldName 属性名称
     * @return 返回 Integer
     */
    public static Integer getInteger(Map<String, Object> dataMap, String fieldName) {
        return getInteger(dataMap, fieldName, null);
    }

    /**
     * 将 DataMap 指定属性转换成 long 类型
     *
     * @param dataMap      数据MAP
     * @param fieldName    属性名称
     * @param defaultValue 默认值
     * @return 返回Long
     */
    public static Long getLong(Map<String, Object> dataMap, String fieldName, Long defaultValue) {
        return ConvertUtil.toLong(getObject(dataMap, fieldName), defaultValue);
    }

    /**
     * 将 DataMap 指定属性转换成 long 类型
     *
     * @param dataMap   数据MAP
     * @param fieldName 属性名称
     * @return 返回Long
     */
    public static Long getLong(Map<String, Object> dataMap, String fieldName) {
        return getLong(dataMap, fieldName, null);
    }

    /**
     * 将 DataMap 指定属性转换成 float 类型
     *
     * @param dataMap      数据MAP
     * @param fieldName    属性名称
     * @param defaultValue 默认值
     * @return 返回Float
     */
    public static Float getFloat(Map<String, Object> dataMap, String fieldName, Float defaultValue) {
        return ConvertUtil.toFloat(getObject(dataMap, fieldName), defaultValue);
    }

    /**
     * 将 DataMap 指定属性转换成 float 类型
     *
     * @param dataMap   数据MAP
     * @param fieldName 属性名称
     * @return 返回Float
     */
    public static Float getFloat(Map<String, Object> dataMap, String fieldName) {
        return getFloat(dataMap, fieldName, null);
    }

    /**
     * 将 DataMap 指定属性转换成 Double 类型
     *
     * @param dataMap      数据MAP
     * @param fieldName    属性名称
     * @param defaultValue 默认值
     * @return 返回Double
     */
    public static Double getDouble(Map<String, Object> dataMap, String fieldName, Double defaultValue) {
        return ConvertUtil.toDouble(getObject(dataMap, fieldName), defaultValue);
    }

    /**
     * 将 DataMap 指定属性转换成 Double 类型
     *
     * @param dataMap   数据MAP
     * @param fieldName 属性名称
     * @return 返回Double
     */
    public static Double getDouble(Map<String, Object> dataMap, String fieldName) {
        return getDouble(dataMap, fieldName, null);
    }

    /**
     * 将 DataMap 指定属性转换成 Boolean 类型
     * 以下情况会转换成true: true, yes, ok, 1, yeah, on, open
     * 以下情况会转换成false: false, no, not, 0, close
     * 非true or false 则返回默认值
     * <p>
     * 注： 以上匹配均忽略大小写
     *
     * @param dataMap      数据MAP
     * @param fieldName    属性名称
     * @param defaultValue 默认值
     * @return 返回Boolean
     */
    public static Boolean getBoolean(Map<String, Object> dataMap, String fieldName, Boolean defaultValue) {
        return ConvertUtil.toBoolean(getObject(dataMap, fieldName), defaultValue);
    }

    /**
     * 将 DataMap 指定属性转换成 Boolean 类型
     *
     * @param dataMap   数据MAP
     * @param fieldName 属性名称
     * @return 返回Boolean
     */
    public static Boolean getBoolean(Map<String, Object> dataMap, String fieldName) {
        return getBoolean(dataMap, fieldName, null);
    }

    /**
     * 将 DataMap 指定属性转换成 Date 类型
     *
     * @param dataMap      数据MAP
     * @param fieldName    属性名称
     * @param defaultValue 默认值
     * @return 返回Date
     */
    public static Date getDate(Map<String, Object> dataMap, String fieldName, Date defaultValue) {
        return ConvertUtil.toDate(getObject(dataMap, fieldName), defaultValue);
    }

    /**
     * 将 DataMap 指定属性转换成 Date 类型
     *
     * @param dataMap   数据MAP
     * @param fieldName 属性名称
     * @return 返回Date
     */
    public static Date getDate(Map<String, Object> dataMap, String fieldName) {
        return getDate(dataMap, fieldName, null);
    }

    private static Object getObject(Map<String, Object> dataMap, String fieldName) {
        return null == dataMap ? null : dataMap.get(fieldName);
    }

    public static String getString(Map<String, Object> dataMap, String fieldName, String defaultValue) {
        return ConvertUtil.toString(getObject(dataMap, fieldName), defaultValue);
    }

    /**
     * 将 jsonNode 指定属性转换成 Integer 类型
     *
     * @param jsonNode     数据节点
     * @param expression   查找表达式
     * @param defaultValue 默认值
     * @return 返回Integer
     */
    public static Integer getInteger(JsonNode jsonNode, String expression, Integer defaultValue) {
        return ConvertUtil.toInteger(getObjectJson(jsonNode, expression), defaultValue);
    }

    /**
     * 将 jsonNode 指定属性转换成 int 类型
     *
     * @param jsonNode   数据节点
     * @param expression 查找表达式
     * @return 返回Integer
     */
    public static Integer getInteger(JsonNode jsonNode, String expression) {
        return getInteger(jsonNode, expression, null);
    }

    /**
     * 将 jsonNode 指定属性转换成 long 类型
     *
     * @param jsonNode     数据节点
     * @param expression   查找表达式
     * @param defaultValue 默认值
     * @return 返回Long
     */
    public static Long getLong(JsonNode jsonNode, String expression, Long defaultValue) {
        return ConvertUtil.toLong(getObjectJson(jsonNode, expression), defaultValue);
    }

    /**
     * 将 jsonNode 指定属性转换成 long 类型
     *
     * @param jsonNode   数据节点
     * @param expression 查找表达式
     * @return 返回Long
     */
    public static Long getLong(JsonNode jsonNode, String expression) {
        return getLong(jsonNode, expression, null);
    }

    /**
     * 将 jsonNode 指定属性转换成 float 类型
     *
     * @param jsonNode     数据节点
     * @param expression   查找表达式
     * @param defaultValue 默认值
     * @return 返回Float
     */
    public static Float getFloat(JsonNode jsonNode, String expression, Float defaultValue) {
        return ConvertUtil.toFloat(getObjectJson(jsonNode, expression), defaultValue);
    }

    /**
     * 将 jsonNode 指定属性转换成 float 类型
     *
     * @param jsonNode   数据节点
     * @param expression 查找表达式
     * @return 返回Float
     */
    public static Float getFloat(JsonNode jsonNode, String expression) {
        return getFloat(jsonNode, expression, null);
    }

    /**
     * 将 jsonNode 指定属性转换成 Double 类型
     *
     * @param jsonNode     数据节点
     * @param expression   查找表达式
     * @param defaultValue 默认值
     * @return 返回Double
     */
    public static Double getDouble(JsonNode jsonNode, String expression, Double defaultValue) {
        return ConvertUtil.toDouble(getObjectJson(jsonNode, expression), defaultValue);
    }

    /**
     * 将 jsonNode 指定属性转换成 Double 类型
     *
     * @param jsonNode   数据节点
     * @param expression 查找表达式
     * @return 返回Double
     */
    public static Double getDouble(JsonNode jsonNode, String expression) {
        return getDouble(jsonNode, expression, null);
    }

    /**
     * 将 jsonNode 指定属性转换成 Boolean 类型
     * 以下情况会转换成true: true, yes, ok, 1, yeah, on, open
     * 以下情况会转换成false: false, no, not, 0, close
     * 非true or false 则返回默认值
     * <p>
     * 注： 以上匹配均忽略大小写
     *
     * @param jsonNode     数据节点
     * @param expression   查找表达式
     * @param defaultValue 默认值
     * @return 返回Boolean
     */
    public static Boolean getBoolean(JsonNode jsonNode, String expression, Boolean defaultValue) {
        return ConvertUtil.toBoolean(getObjectJson(jsonNode, expression), defaultValue);
    }

    /**
     * 将 jsonNode 指定属性转换成 Boolean 类型
     *
     * @param jsonNode   数据节点
     * @param expression 查找表达式
     * @return 返回Boolean
     */
    public static Boolean getBoolean(JsonNode jsonNode, String expression) {
        return getBoolean(jsonNode, expression, null);
    }

    /**
     * 将 jsonNode 指定属性转换成 Date 类型
     *
     * @param jsonNode     数据节点
     * @param expression   查找表达式
     * @param defaultValue 默认值
     * @return 返回Date
     */
    public static Date getDate(JsonNode jsonNode, String expression, Date defaultValue) {
        return ConvertUtil.toDate(getObjectJson(jsonNode, expression), defaultValue);
    }

    /**
     * 将 jsonNode 指定属性转换成 Date 类型
     *
     * @param jsonNode   数据节点
     * @param expression 查找表达式
     * @return 返回Date
     */
    public static Date getDate(JsonNode jsonNode, String expression) {
        return getDate(jsonNode, expression, null);
    }

    /**
     * 转换对象
     *
     * @param json       数据
     * @param expression 表达式
     * @param valueType  值的类型
     * @param <T>        值类型
     * @return 返回对象
     */
    public static <T> T toObject(String json, String expression, Class<T> valueType) {
        if (StringUtils.isBlank(json)) {
            return null;
        }

        JsonNode jsonNode = toJsonNode(json);

        String objectJson = getObjectJson(jsonNode, expression);

        return toObject(objectJson, valueType);
    }

    /**
     * 转换对象List
     *
     * @param json       数据
     * @param expression 表达式
     * @param valueType  值的类型
     * @param <T>        值类型
     * @return 返回对象列表
     */
    public static <T> List<T> toObjectList(String json, String expression, Class<T> valueType) {
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>(0);
        }

        JsonNode jsonNode = toJsonNode(json);

        String objectJson = getObjectJson(jsonNode, expression);

        return toObjectList(objectJson, valueType);
    }

    /**
     * 转换对象
     *
     * @param jsonNode   数据节点
     * @param expression 表达式
     * @param valueType  值的类型
     * @param <T>        值类型
     * @return 返回对象
     */
    public static <T> T toObject(JsonNode jsonNode, String expression, Class<T> valueType) {

        String objectJson = getObjectJson(jsonNode, expression);

        return toObject(objectJson, valueType);
    }

    /**
     * 转换对象 list
     *
     * @param jsonNode   数据节点
     * @param expression 表达式
     * @param valueType  值的类型
     * @param <T>        值类型
     * @return 返回对象列表
     */
    public static <T> List<T> toObjectList(JsonNode jsonNode, String expression, Class<T> valueType) {

        String objectJson = getObjectJson(jsonNode, expression);

        return toObjectList(objectJson, valueType);
    }

    /**
     * 根据 JSON 数据节点获取指定 表达式的对象
     *
     * @param jsonNode   json数据节点
     * @param expression 表达式, 如果为空则返回节点数据本身
     * @return 返回json字符串
     */
    public static String getObjectJson(JsonNode jsonNode, String expression) {

        JsonNode dataJsonNode = getJsonNode(jsonNode, expression);

        if (null == dataJsonNode) {
            return null;
        }

        return dataJsonNode.toString();
    }

    /**
     * 根据 JSON 数据节点获取指定 表达式的对象
     *
     * @param json       json数据
     * @param expression 表达式, 如果为空则返回节点数据本身
     * @return 返回json字符串
     */
    public static String getObjectJson(String json, String expression) {

        JsonNode dataJsonNode = getJsonNode(json, expression);

        if (null == dataJsonNode) {
            return null;
        }

        return dataJsonNode.toString();
    }

    /**
     * 获取指定json指定表达式的json数据节点
     *
     * @param json       json数据
     * @param expression 表达式
     * @return 返回JsonNode
     */
    public static JsonNode getJsonNode(String json, String expression) {
        return getJsonNode(toJsonNode(json), expression);
    }

    /**
     * 根据 JSON 数据节点获取指定 表达式的数据节点
     *
     * @param jsonNode   json数据节点
     * @param expression 表达式, 如果为空则返回节点数据本身
     * @return 返回JsonNode
     */
    public static JsonNode getJsonNode(JsonNode jsonNode, String expression) {
        if (null == jsonNode) {
            return null;
        }
        if (StringUtils.isBlank(expression)) {
            return jsonNode;
        }

        // 切分属性
        String[] fieldNames = expression.split("\\.");
        JsonNode lastJsonNode = jsonNode;
        for (String directFieldExpress : fieldNames) {

            JsonNode tempJsonNode = getSubJsonNode(lastJsonNode, directFieldExpress);
            if (null == tempJsonNode) {
                return null;
            }
            lastJsonNode = tempJsonNode;
        }

        return lastJsonNode;
    }

    /**
     * 获取子节点
     *
     * @param jsonNode           json 数据节点
     * @param directFieldExpress 直接属性表达式
     * @return 返回JsonNode
     */
    private static JsonNode getSubJsonNode(JsonNode jsonNode, String directFieldExpress) {

        if (null == jsonNode || StringUtils.isBlank(directFieldExpress)) {
            return jsonNode;
        }

        String arrayFieldNameRegex = "(?i)^([a-z0-9_]+)?(\\[(\\d+)\\])?$";
        String realFieldName = directFieldExpress.replaceAll(arrayFieldNameRegex, "$1");
        String indexString = directFieldExpress.replaceAll(arrayFieldNameRegex, "$3");

        JsonNode subJsonNode = jsonNode;
        if (StringUtils.isNotBlank(realFieldName)) {
            subJsonNode = jsonNode.get(realFieldName);
            if (null == subJsonNode) {
                return null;
            }
        }

        if (StringUtils.isNotBlank(indexString)) {
            int index = ConvertUtil.toInteger(indexString, 0);
            subJsonNode = subJsonNode.get(index);
        }

        return subJsonNode;
    }

}
