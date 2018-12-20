package com.duowan.common.jdbc;

import com.duowan.common.jdbc.definition.QueryCondDef;
import com.duowan.common.jdbc.exception.JdbcException;
import com.duowan.common.utils.AssertUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 模型定义上下文
 *
 * @author Arvin
 * @since 2018/1/1 8:36
 */
public class MDContext {

    private MDContext() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * ModelDef Map
     */
    private static final Map<Class<?>, ModelDef> MD_MAP = new HashMap<>();

    /**
     * 查询条件类型
     */
    private static final Map<Class<?>, QueryCondDef> QUERY_COND_DEF_MAP = new HashMap<>();

    /**
     * 获取指定模型类对应的 模型定义
     *
     * @param modelType 模型类
     * @return 返回模型定义，如果没有的话直接抛出异常
     */
    public static ModelDef getMD(Class<?> modelType) {
        ModelDef md = MD_MAP.get(modelType);
        if (null != md) {
            return md;
        }
        try {
            md = new ModelDef(modelType);

            MD_MAP.put(modelType, md);

            return md;
        } catch (Exception e) {
            throw new JdbcException(e.getMessage(), e);
        }
    }

    /**
     * 获取查询条件定义
     *
     * @param queryConditionType 查询条件类型
     * @return 返回一个查询条件定义
     */
    public static QueryCondDef getQueryCondDef(Class<?> queryConditionType) {
        return registerQueryConditionTypeIfNotRegister(queryConditionType);
    }

    /**
     * 如果没有注册过的话就进行注册
     *
     * @param queryConditionType 查询条件类型
     * @return 返回查询条件定义
     */
    private static QueryCondDef registerQueryConditionTypeIfNotRegister(Class<?> queryConditionType) {
        QueryCondDef queryConditionDefinition = QUERY_COND_DEF_MAP.get(queryConditionType);
        if (null == queryConditionDefinition) {
            return registerQueryConditionType(queryConditionType);
        }
        AssertUtil.assertNotNull(queryConditionDefinition, "无法解析查询条件定义： " + queryConditionType.getSimpleName());
        return queryConditionDefinition;
    }

    /**
     * 注册查询条件类型，字段，表名，等等
     *
     * @param queryConditionType 模型类型
     * @return 返回查询条件定义
     */
    public static QueryCondDef registerQueryConditionType(Class<?> queryConditionType) {
        if (null != queryConditionType) {
            return QUERY_COND_DEF_MAP.computeIfAbsent(queryConditionType, key -> new QueryCondDef(queryConditionType));
        }
        return null;
    }


    public static String getTableName(Class<?> modelType) {
        return getMD(modelType).getTableName();
    }

    public static String getColumnName(Class<?> modelType, String fieldName) {
        return getMD(modelType).getColumnName(fieldName);
    }
}
