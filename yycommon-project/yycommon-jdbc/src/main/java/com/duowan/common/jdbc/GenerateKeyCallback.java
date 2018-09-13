package com.duowan.common.jdbc;

/**
 * 自增（数据库生成的KEY回调函数）
 *
 * @author Arvin
 */
public interface GenerateKeyCallback {

    /**
     * 数据库自生成 key 回调函数
     *
     * @param rowIndex   插入数据的索引，即第几行插入的, 从 1 开始
     * @param primaryKey 主键值
     */
    void call(int rowIndex, Object primaryKey);
}
