package com.duowan.common.jdbc;

import com.duowan.common.jdbc.annotations.Ignore;
import com.duowan.common.jdbc.annotations.Table;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.ReflectUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 模型定义
 *
 * @author Arvin
 * @since 2018/5/21 20:39
 */
public class ModelDef {

    /**
     * 模型类
     */
    private Class<?> type;

    /**
     * 数据库对应的表名称
     */
    private String tableName;

    /**
     * 自增主键属性定义，一个表只能有一个
     */
    private FieldDef autoIncrementFieldDef;

    /**
     * 主键属性定义列表
     */
    private List<FieldDef> primaryKeyDefList = new ArrayList<>();

    /**
     * 唯一属性定义列表
     */
    private List<FieldDef> uniqueKeyDefList = new ArrayList<>();

    /**
     * 普通属性定义列表
     */
    private List<FieldDef> normalDefList = new ArrayList<>();

    /**
     * 所有的属性定义
     */
    private List<FieldDef> allDefList = new ArrayList<>();

    /**
     * 数据库列名称 --> 属性定义
     */
    private Map<String, FieldDef> columnToFieldDefMap = new HashMap<>();

    /**
     * Java 属性名称 --> 属性定义
     */
    private Map<String, FieldDef> fieldNameToFieldDefMap = new HashMap<>();

    /**
     * Java 属性 --> 属性定义
     */
    private Map<Field, FieldDef> fieldToFieldDefMap = new HashMap<>();

    public ModelDef(Class<?> type) throws Exception {
        this.type = type;

        // 初始化模型定义
        init();
    }

    private void init() {

        Table tableAnn = ReflectUtil.getClassAnnotation(this.type, Table.class);
        if (null == tableAnn) {
            this.tableName = CommonUtil.humpToUnderline(this.type.getSimpleName());
        } else {
            String tableName = tableAnn.tablename();
            if (StringUtils.isNotBlank(tableName)) {
                this.tableName = tableName;
            } else {
                if (tableAnn.underline()) {
                    this.tableName = CommonUtil.humpToUnderline(this.type.getSimpleName());
                } else {
                    this.tableName = CommonUtil.firstLetterToLowerCase(this.type.getSimpleName());
                }
            }
        }

        // 初始化属性
        initFieldDef();

    }

    private void initFieldDef() {
        List<Field> fieldList = ReflectUtil.getAllNoneStaticDeclaredFields(this.getType());

        if (fieldList == null || fieldList.isEmpty()) {
            return;
        }

        for (Field field : fieldList) {
            if (isIgnoreField(field)) {
                continue;
            }

            FieldDef fd = new FieldDef(this, field);

            if (fd.isPrimaryKey()) {
                this.primaryKeyDefList.add(fd);
                if (fd.isAutoIncrement()) {
                    AssertUtil.assertNull(this.autoIncrementFieldDef, "当前模型[" + this.getType().getName() + "]只能设定一个自增主键！");
                    this.autoIncrementFieldDef = fd;
                }
            }

            if (fd.isUniqueKey()) {
                this.uniqueKeyDefList.add(fd);
            }

            if (!fd.isPrimaryKey() && !fd.isUniqueKey()) {
                this.normalDefList.add(fd);
            }

            this.allDefList.add(fd);

            this.columnToFieldDefMap.put(fd.getColumnName(), fd);
            this.fieldNameToFieldDefMap.put(fd.getFieldName(), fd);
            this.fieldToFieldDefMap.put(fd.getField(), fd);

            // 排序的规则： 主键，唯一键， 其他字段
            Collections.sort(this.allDefList);
        }

    }


    /**
     * java属性名称 --> 忽略的java属性
     */
    private Map<String, Field> modelFieldNameToIgnoredModelFieldMap = new HashMap<String, Field>();

    private static final String SERIAL_VERSION_ID = "serialVersionUID";

    /**
     * 是否是忽略的属性
     *
     * @param field 属性
     * @return true 表示要忽略
     */
    private boolean isIgnoreField(Field field) {

        if (SERIAL_VERSION_ID.equalsIgnoreCase(field.getName())) {
            return true;
        }

        Ignore ignoreAnn = ReflectUtil.getFieldAnnotation(field, Ignore.class);

        if (null == ignoreAnn) {
            return false;
        }

        modelFieldNameToIgnoredModelFieldMap.put(field.getName(), field);

        return true;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public FieldDef getAutoIncrementFieldDef() {
        return autoIncrementFieldDef;
    }

    public void setAutoIncrementFieldDef(FieldDef autoIncrementFieldDef) {
        this.autoIncrementFieldDef = autoIncrementFieldDef;
    }

    public List<FieldDef> getPrimaryKeyDefList() {
        return primaryKeyDefList;
    }

    public void setPrimaryKeyDefList(List<FieldDef> primaryKeyDefList) {
        this.primaryKeyDefList = primaryKeyDefList;
    }

    public List<FieldDef> getUniqueKeyDefList() {
        return uniqueKeyDefList;
    }

    public void setUniqueKeyDefList(List<FieldDef> uniqueKeyDefList) {
        this.uniqueKeyDefList = uniqueKeyDefList;
    }

    public List<FieldDef> getNormalDefList() {
        return normalDefList;
    }

    public void setNormalDefList(List<FieldDef> normalDefList) {
        this.normalDefList = normalDefList;
    }

    public List<FieldDef> getAllDefList() {
        return allDefList;
    }

    public void setAllDefList(List<FieldDef> allDefList) {
        this.allDefList = allDefList;
    }

    public Map<String, FieldDef> getColumnToFieldDefMap() {
        return columnToFieldDefMap;
    }

    public void setColumnToFieldDefMap(Map<String, FieldDef> columnToFieldDefMap) {
        this.columnToFieldDefMap = columnToFieldDefMap;
    }

    public Map<String, FieldDef> getFieldNameToFieldDefMap() {
        return fieldNameToFieldDefMap;
    }

    public void setFieldNameToFieldDefMap(Map<String, FieldDef> fieldNameToFieldDefMap) {
        this.fieldNameToFieldDefMap = fieldNameToFieldDefMap;
    }

    public Map<Field, FieldDef> getFieldToFieldDefMap() {
        return fieldToFieldDefMap;
    }

    public void setFieldToFieldDefMap(Map<Field, FieldDef> fieldToFieldDefMap) {
        this.fieldToFieldDefMap = fieldToFieldDefMap;
    }

    public String getColumnName(String fieldName) {
        return this.fieldNameToFieldDefMap.get(fieldName).getColumnName();
    }

    public FieldDef getFieldDefByModelFieldName(String fieldName) {
        return this.fieldNameToFieldDefMap.get(fieldName);
    }

    public FieldDef getFieldDefByColumn(String columnName) {
        return this.columnToFieldDefMap.get(columnName);
    }

    public boolean hasAutoIncrementKey() {
        return autoIncrementFieldDef != null;
    }
}
