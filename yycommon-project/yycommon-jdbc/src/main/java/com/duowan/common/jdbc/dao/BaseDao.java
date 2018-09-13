package com.duowan.common.jdbc.dao;

import com.duowan.common.jdbc.Page;

import java.util.List;

/**
 * 基础DAO接口
 *
 * @author Arvin
 */
public interface BaseDao<PK, T, Q> {

    /**
     * 插入，并自动填充 自增主键和 uuid 主键
     *
     * @param model 要插入的模型对象
     * @return 返回数据库操作影响行数
     */
    int insert(T model);

    /**
     * 根据主键删除
     *
     * @param primaryKey 主键ID
     * @return 返回数据库操作影响行数
     */
    int delete(PK primaryKey);

    /**
     * 根据主键查询
     *
     * @param primaryKey 主键
     * @return 如果存在就返回对象，否则返回null
     */
    T get(PK primaryKey);

    /**
     * 根据主键更新数据
     *
     * @param model 模型对象
     * @return 返回数据库操作影响行数
     */
    int update(T model);

    /**
     * 查询列表
     *
     * @param queryCondition 查询条件
     * @param pageNo         页码
     * @param pageSize       查询数量
     * @return 返回结果
     */
    List<T> queryList(Q queryCondition, int pageNo, int pageSize);

    /**
     * 分页查询
     *
     * @param queryCondition 查询条件
     * @param pageNo         页码
     * @param pageSize       查询数量
     * @return 返回结果
     */
    Page<T> queryPage(Q queryCondition, int pageNo, int pageSize);
}
