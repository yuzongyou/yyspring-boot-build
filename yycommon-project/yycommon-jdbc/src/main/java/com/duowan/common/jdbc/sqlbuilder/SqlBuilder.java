package com.duowan.common.jdbc.sqlbuilder;

/**
 * @author Arvin
 */
public interface SqlBuilder {

    /**
     * 获取参数
     *
     * @return 返回sql需要的参数
     */
    Object[] getParams();

    /**
     * 获取sql
     *
     * @return 返回构造的sql
     */
    String getSql();
}
