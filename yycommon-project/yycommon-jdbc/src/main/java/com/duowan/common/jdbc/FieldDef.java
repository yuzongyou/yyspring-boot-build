package com.duowan.common.jdbc;

import  com.duowan.common.jdbc.annotations.Column;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.ReflectUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * 数据库属性定义
 *
 * @author Arvin
 */
public class FieldDef implements Comparable<FieldDef> {

    /**
     * 所属模型定义
     */
    private ModelDef modelDef;

    /**
     * 属性对象
     */
    private Field field;

    /**
     * 属性类型
     */
    private Class<?> fieldType;

    /**
     * 属性名称
     */
    private String fieldName;

    /**
     * 列名称，不包装
     */
    private String columnName;

    /**
     * 是否主键
     */
    private boolean primaryKey = false;

    /**
     * 是否自增, primaryKey = true 时生效
     */
    private boolean autoIncrement = false;

    /**
     * 是否使用UUID
     */
    private boolean useUuid = false;

    /**
     * 是否是唯一能表示数据的属性
     */
    private boolean uniqueKey = false;

    /**
     * 插入的时候是否忽略
     */
    private boolean insertIgnore = false;

    /**
     * 插入的时候是否忽略 null 值
     */
    private boolean insertIgnoreNull = true;

    /**
     * 更新的时候是否忽略，默认不忽略
     */
    private boolean updateIgnore = false;

    /**
     * 更新的时候是否忽略 null 值, 默认忽略
     */
    private boolean updateIgnoreNull = true;

    public FieldDef(ModelDef modelDef, Field field) {
        AssertUtil.assertNotNull(modelDef, "模型定义类不能为空！");
        AssertUtil.assertNotNull(field, "模型属性不能为空！");

        this.modelDef = modelDef;
        this.field = field;
        this.fieldType = field.getType();
        this.fieldName = field.getName();

        init();
    }

    private void init() {
        Column columnAnn = ReflectUtil.getFieldAnnotation(this.getField(), Column.class);

        initColumnName(columnAnn);

        this.primaryKey = columnAnn != null && columnAnn.primaryKey();

        initAutoIncrement(columnAnn);

        initUseUuid(columnAnn);

        initUniqueKey(columnAnn);

        initInsertIgnore(columnAnn);

        initInsertIgnoreNull(columnAnn);

        initUpdateIgnore(columnAnn);

        initUpdateIgnoreNull(columnAnn);

    }

    private void initUpdateIgnoreNull(Column columnAnn) {
        if (null != columnAnn) {
            this.updateIgnoreNull = columnAnn.updateIgnoreNull();
        }
    }

    private void initUpdateIgnore(Column columnAnn) {
        if (null != columnAnn) {
            this.updateIgnore = columnAnn.updateIgnore();
        }
    }

    private void initInsertIgnoreNull(Column columnAnn) {
        if (null != columnAnn) {
            this.insertIgnoreNull = columnAnn.insertIgnoreNull();
        }
    }

    private void initInsertIgnore(Column columnAnn) {
        if (null != columnAnn) {
            this.insertIgnore = columnAnn.insertIgnore();
        }
    }

    private void initUniqueKey(Column columnAnn) {
        if (null != columnAnn) {
            this.uniqueKey = columnAnn.uniqueKey();
        }
    }

    private void initUseUuid(Column columnAnn) {

        this.useUuid = null != columnAnn && columnAnn.useUuid();

        boolean allowUseUuid = JdbcHelper.isAllowUuidType(this.fieldType);

        if (this.useUuid) {
            AssertUtil.assertTrue(allowUseUuid, "字段[" + fieldName + "]不是字符串类型，不支持UUID");
        }

    }

    private void initAutoIncrement(Column columnAnn) {
        boolean allowAutoIncrement = JdbcHelper.isAllowAutoIncrementType(this.fieldType);
        this.autoIncrement = null != columnAnn && columnAnn.autoIncrement();

        if (!this.primaryKey) {
            AssertUtil.assertFalse(this.autoIncrement, "字段[" + fieldName + "]不是主键，不能设置 autoIncrement = true");
        }

        AssertUtil.assertFalse(!allowAutoIncrement && this.autoIncrement, "字段[" + fieldName + "]设置了 autoIncrement=true, 但是该字段类型[" + fieldType.getName() + "]不支持自增, 允许自增的字段是[" + JdbcHelper.getAllowAutoIncrementTypeString() + "]");

    }

    private void initColumnName(Column columnAnn) {
        if (null != columnAnn) {
            String columnName = columnAnn.columnName();
            if (StringUtils.isNotBlank(columnName)) {
                this.columnName = columnName;
                return;
            }
        }
        // 默认使用下划线
        this.columnName = CommonUtil.humpToUnderline(this.getFieldName());
    }

    public ModelDef getModelDef() {
        return modelDef;
    }

    public void setModelDef(ModelDef modelDef) {
        this.modelDef = modelDef;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean isUseUuid() {
        return useUuid;
    }

    public void setUseUuid(boolean useUuid) {
        this.useUuid = useUuid;
    }

    public boolean isUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(boolean uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public boolean isInsertIgnore() {
        return insertIgnore;
    }

    public void setInsertIgnore(boolean insertIgnore) {
        this.insertIgnore = insertIgnore;
    }

    public boolean isInsertIgnoreNull() {
        return insertIgnoreNull;
    }

    public void setInsertIgnoreNull(boolean insertIgnoreNull) {
        this.insertIgnoreNull = insertIgnoreNull;
    }

    public boolean isUpdateIgnore() {
        return updateIgnore;
    }

    public void setUpdateIgnore(boolean updateIgnore) {
        this.updateIgnore = updateIgnore;
    }

    public boolean isUpdateIgnoreNull() {
        return updateIgnoreNull;
    }

    public void setUpdateIgnoreNull(boolean updateIgnoreNull) {
        this.updateIgnoreNull = updateIgnoreNull;
    }

    @Override
    public int compareTo(FieldDef that) {
        // 排序的规则： 主键，唯一键， 其他字段
        boolean selfIsPrimaryKey = isPrimaryKey();
        boolean thatIsPrimaryKey = that.isPrimaryKey();

        if (selfIsPrimaryKey && thatIsPrimaryKey) {
            return 0;
        }
        if (selfIsPrimaryKey) {
            return -1;
        }
        if (thatIsPrimaryKey) {
            return 1;
        }

        boolean selfIsUniqueKey = isUniqueKey();
        boolean thatIsUniqueKey = that.isUniqueKey();
        if (selfIsUniqueKey && thatIsUniqueKey) {
            return 0;
        }
        if (selfIsUniqueKey) {
            return -1;
        }
        if (thatIsUniqueKey) {
            return 1;
        }

        return 0;
    }
}
