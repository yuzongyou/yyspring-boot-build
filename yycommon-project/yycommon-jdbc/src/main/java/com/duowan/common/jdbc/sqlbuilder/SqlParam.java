package com.duowan.common.jdbc.sqlbuilder;

import com.duowan.common.jdbc.FieldDef;

/**
 * Sql 参数
 *
 * @author Arvin
 */
public class SqlParam {

    /**
     * 值
     */
    private Object value;

    /**
     * 参数类型
     */
    private Class<?> paramType;

    /**
     * 对应列定义
     */
    private FieldDef fd;

    public SqlParam(FieldDef fieldDefinition, Object value) {
        this.fd = fieldDefinition;
        this.value = value;
        this.paramType = fieldDefinition.getFieldType();
    }

    public SqlParam(Object value) {
        this.value = value;
        this.paramType = null == value ? null : value.getClass();
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Class<?> getParamType() {
        return paramType;
    }

    public String getParamTypeName() {
        return paramType == null ? null : paramType.getName();
    }

    public String getParamTypeSimpleName() {
        return paramType == null ? null : paramType.getSimpleName();
    }

    public void setParamType(Class<?> paramType) {
        this.paramType = paramType;
    }

    public FieldDef getFd() {
        return fd;
    }

    public void setFd(FieldDef fieldDefinition) {
        this.fd = fieldDefinition;
    }

    public String getModelFieldName() {
        return null != fd ? fd.getFieldName() : null;
    }

    public String getColumnName() {
        return null != fd ? fd.getColumnName() : null;
    }
}
