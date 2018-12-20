package com.duowan.common.jdbc.sqlbuilder;

import com.duowan.common.jdbc.*;
import com.duowan.common.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Sql Builder 构建基类
 *
 * @author Arvin
 */
public abstract class AbstractSqlBuilder<T> {

    protected final Logger logger;

    /**
     * 参数列表
     */
    private List<Object> params = new ArrayList<>();

    /**
     * 参数类型列表
     */
    private List<Class<?>> paramTypes = new ArrayList<>();

    /**
     * sql 参数列表
     */
    private List<SqlParam> sqlParamList = new ArrayList<>();

    /**
     * 要忽略不进行处理的 Java 字段名称
     */
    private Set<String> ignoreModelFields = new HashSet<>();

    /**
     * 要处理的模型类型
     */
    private final Class<?> modelType;

    /**
     * sql 构建类型
     */
    private final SqlBuilderType sqlBuilderType;

    /**
     * 模型定义
     */
    private final ModelDef md;

    /**
     * 如果设置了自定义的表名的话，就使用自定义的表名，主要是处理分表的情况
     */
    private String customTableName;

    public AbstractSqlBuilder(Class<?> modelType, SqlBuilderType sqlBuilderType) {

        AssertUtil.assertNotNull(modelType, "The model class should not be null!");
        AssertUtil.assertNotNull(sqlBuilderType, "The sql builder type should not be null!");

        // 初始化日志
        this.logger = LoggerFactory.getLogger(this.getClass().getPackage().getName() + "." + sqlBuilderType.name());

        this.modelType = modelType;
        this.sqlBuilderType = sqlBuilderType;

        // 注册模型类型
        this.md = MDContext.getMD(modelType);

    }

    public Class<?> getModelType() {
        return modelType;
    }

    public SqlBuilderType getSqlBuilderType() {
        return sqlBuilderType;
    }

    /**
     * 获取自己
     *
     * @return 返回具体的子类对象
     */
    protected abstract T self();

    /**
     * 重置
     */
    protected void reset() {
        this.params.clear();
        this.paramTypes.clear();
        this.sqlParamList.clear();
    }

    protected int getParamsCount() {
        return null != sqlParamList ? sqlParamList.size() : 0;
    }

    public List<SqlParam> getSqlParamList() {
        return sqlParamList;
    }

    public List<Class<?>> getParamTypes() {
        return paramTypes;
    }

    /**
     * 设置要忽略的Java属性
     *
     * @param ignoreModelFieldNames 设置要忽略的Java属性
     * @return 返回Sql构造器本身
     */
    public T ignoreModelFields(String... ignoreModelFieldNames) {
        if (null != ignoreModelFieldNames && ignoreModelFieldNames.length > 0) {
            // 计算对应字段，添加到 ignoreColumns
            for (String ignoreModelField : ignoreModelFieldNames) {
                if (StringUtils.isNotBlank(ignoreModelField)) {
                    this.ignoreModelFields.add(ignoreModelField);
                }
            }
        }
        return self();
    }

    /**
     * 设置要忽略的Java属性
     *
     * @param ignoreModelFieldNames 设置要忽略的Java属性
     * @return 返回Sql构造器本身
     */
    public T ignoreModelFields(Collection<String> ignoreModelFieldNames) {
        if (null != ignoreModelFieldNames && !ignoreModelFieldNames.isEmpty()) {
            // 计算对应字段，添加到 ignoreColumns
            for (String ignoreModelField : ignoreModelFieldNames) {
                if (StringUtils.isNotBlank(ignoreModelField)) {
                    this.ignoreModelFields.add(ignoreModelField);
                }
            }
        }
        return self();
    }

    public ModelDef getMd() {
        return md;
    }

    public T customTableName(String tableName) {

        if (StringUtils.isNotBlank(tableName)) {
            this.customTableName = wrapTableName(tableName);
        }

        return self();
    }

    /**
     * 包装一个数据库表名称
     *
     * @param tableName 数据库表名称
     * @return 返回包装后的数据库表名称
     */
    protected abstract String wrapTableName(String tableName);

    /**
     * 包装一个数据库表列名
     *
     * @param columnName 数据库表列名称
     * @return 返回包装后的数据库表列名称
     */
    protected abstract String wrapColumnName(String columnName);

    /**
     * 检查是否是要忽略的java属性
     *
     * @param fieldName 要检查的java属性
     * @return 是否忽略
     */
    public boolean isIgnoreModelField(String fieldName) {
        if (StringUtils.isBlank(fieldName)) {
            return true;
        }
        if (this.ignoreModelFields.contains(fieldName)) {
            return true;
        }
        return null == this.getMd().getFieldDefByModelFieldName(fieldName);
    }

    public Object[] getParams() {
        return params.toArray();
    }

    public List<Object> getListParams() {
        return params;
    }

    /**
     * 添加参数
     *
     * @param sqlParams 参数值
     * @return 返回Sql构造器本身
     */
    protected T addSqlParams(SqlParam... sqlParams) {
        if (null != sqlParams && sqlParams.length > 0) {
            for (SqlParam sqlParam : sqlParams) {
                this.sqlParamList.add(sqlParam);
                this.params.add(sqlParam.getValue());
            }
        }
        return self();
    }

    /**
     * 根据参数值计算sql参数
     *
     * @param paramValues 参数值
     * @return 返回Sql构造器本身
     */
    protected T addParamsByValues(Object... paramValues) {
        if (null != paramValues && paramValues.length > 0) {
            List<SqlParam> tempSqlParamList = new ArrayList<>();
            for (Object value : paramValues) {
                tempSqlParamList.add(new SqlParam(value));
            }
            addListSqlParams(tempSqlParamList);
        }
        return self();
    }

    /**
     * 根据列名添加参数
     *
     * @param columnName 列名
     * @param paramValue 参数值
     * @return 返回Sql构造器本身
     */
    protected T addParamByColumnName(String columnName, Object paramValue) {
        FieldDef columnDefinition = getMd().getFieldDefByColumn(columnName);
        return addSqlParams(new SqlParam(columnDefinition, paramValue));
    }

    /**
     * 根据列定义添加参数
     *
     * @param fd         列定义
     * @param paramValue 参数值
     * @return 返回Sql构造器本身
     */
    protected T addParamByColumnDefinition(FieldDef fd, Object paramValue) {
        return addSqlParams(new SqlParam(fd, paramValue));
    }

    /**
     * 根据模型属性名称添加参数
     *
     * @param modelFieldName 列名
     * @param paramValue     参数值
     * @return 返回Sql构造器本身
     */
    protected T addParamByModelFieldName(String modelFieldName, Object paramValue) {
        FieldDef fd = getMd().getFieldDefByModelFieldName(modelFieldName);
        return addSqlParams(new SqlParam(fd, paramValue));
    }

    /**
     * 添加参数
     *
     * @param sqlParams 参数列表
     * @return 返回Sql构造器本身
     */
    protected T addListSqlParams(List<SqlParam> sqlParams) {
        if (null != sqlParams && !sqlParams.isEmpty()) {
            for (SqlParam sqlParam : sqlParams) {
                this.sqlParamList.add(sqlParam);
                this.params.add(sqlParam.getValue());
            }
        }
        return self();
    }

    /**
     * 将参数转成字符串
     *
     * @return 参数字符串
     */
    protected String paramsToString() {
        StringBuilder builder = new StringBuilder("[");
        if (null != params && !params.isEmpty()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Object param : params) {
                if (null != param) {
                    if (JdbcHelper.isStringType(param.getClass())) {
                        builder.append("'").append(param).append("', ");
                    } else if (JdbcHelper.isTimeType(param.getClass())) {
                        builder.append("'").append(simpleDateFormat.format(JdbcHelper.convertToJavaUtilDate(param))).append("', ");
                    } else {
                        builder.append(param).append(", ");
                    }
                } else {
                    builder.append("NULL, ");
                }
            }
            builder.setLength(builder.length() - 2);
        }
        return builder.append("]").toString();
    }

    /**
     * 获取包裹过的数据库表名称
     *
     * @return 返回数据库表名称
     */
    public String getWrapTableName() {
        // 优先使用自定义的表名称
        if (StringUtils.isNotBlank(this.customTableName)) {
            return wrapTableName(this.customTableName);
        }
        return wrapTableName(this.getMd().getTableName());
    }

    public List<FieldDef> getFdList() {
        return this.md.getAllDefList();
    }

    public List<FieldDef> getPrimaryKeyFdList() {
        return this.md.getPrimaryKeyDefList();
    }

    public List<FieldDef> getUniqueKeyFdList() {
        return this.md.getUniqueKeyDefList();
    }

    public void checkModelFieldName(String modelFieldName) {
        AssertUtil.assertTrue(null != this.md.getFieldDefByModelFieldName(modelFieldName),
                this.modelType.getName() + "." + modelFieldName + " 属性不存在！");
    }

    public void checkColumnName(String columnName) {
        AssertUtil.assertTrue(null != this.md.getFieldDefByColumn(columnName),
                this.md.getTableName() + "." + columnName + " 字段不存在！");
    }

    public FieldDef getFieldDefByModelFieldName(String modelFieldName) {
        return getMd().getFieldDefByModelFieldName(modelFieldName);
    }

}
