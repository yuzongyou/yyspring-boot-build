package com.duowan.yyspringboot.autoconfigure.mybatis.multids.ds2;

import com.duowan.yyspringboot.autoconfigure.mybatis.MybatisDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 单个数据源Mapper
 *
 * @author Arvin
 * @version 1.0
 * @since 2019/1/9 9:18
 */
@Mapper
@MybatisDao
public interface Ds2Mapper {

    @Select("show databases")
    List<String> getDatabases();

    @Select("show tables")
    List<String> getTables();

    String selectSelf(@Param("self") String self);
}
