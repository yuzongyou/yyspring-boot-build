package com.duowan.mybatis.dao.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2019/1/9 14:39
 */
@Mapper
public interface SampleMapper {

    @Select("show databases")
    List<String> getDatabases();

    @Select("show tables")
    List<String> getTables();

    /**
     * 这个基于 Mapper.xml 实现
     */
    String selectSelf(@Param("self") String self);
}
