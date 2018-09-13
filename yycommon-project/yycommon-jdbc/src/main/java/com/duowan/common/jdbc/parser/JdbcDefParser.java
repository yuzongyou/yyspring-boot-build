package com.duowan.common.jdbc.parser;

import com.duowan.common.jdbc.model.JdbcDef;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;

/**
 * JdbcDef 定义解析器
 *
 * @author Arvin
 */
public interface JdbcDefParser {

    /**
     * 解析 Jdbc 定义
     *
     * @param configMap   数据源配置MAP
     * @param environment 系统环境
     * @return 返回 jdbc 定义列表
     */
    List<? extends JdbcDef> parser(Map<String, String> configMap, Environment environment);
}
