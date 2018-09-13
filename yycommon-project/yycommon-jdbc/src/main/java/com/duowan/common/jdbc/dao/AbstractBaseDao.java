package com.duowan.common.jdbc.dao;

import com.duowan.common.jdbc.Jdbc;
import com.duowan.common.jdbc.Page;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Arvin
 * @since 2018/5/22 17:01
 */
public abstract class AbstractBaseDao<PK, T, Q> implements BaseDao<PK, T, Q> {

    protected final Class<PK> pkType;
    protected final Class<T> modelType;
    protected final Class<Q> queryType;

    @SuppressWarnings({"unchecked"})
    public AbstractBaseDao() {
        Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();

        this.pkType = (Class<PK>) types[0];
        this.modelType = (Class<T>) types[1];
        this.queryType = (Class<Q>) types[2];
    }

    /**
     * 获取JDBC实例
     *
     * @return 返回 JDBC 实例
     */
    protected abstract Jdbc getJdbc();

    @Override
    public int insert(T model) {
        return getJdbc().insert(model, true);
    }

    @Override
    public int delete(PK primaryKey) {
        return getJdbc().deleteByPrimaryKey(modelType, primaryKey);
    }

    @Override
    public T get(PK primaryKey) {
        return getJdbc().queryByPrimaryKey(modelType, primaryKey);
    }

    @Override
    public int update(T model) {
        return getJdbc().updateByPrimaryKey(model);
    }

    @Override
    public List<T> queryList(Q queryCondition, int pageNo, int pageSize) {
        return getJdbc().queryModelList(modelType, queryCondition, pageNo, pageSize);
    }

    @Override
    public Page<T> queryPage(Q queryCondition, int pageNo, int pageSize) {
        return getJdbc().queryPage(modelType, queryCondition, pageNo, pageSize);
    }
}
