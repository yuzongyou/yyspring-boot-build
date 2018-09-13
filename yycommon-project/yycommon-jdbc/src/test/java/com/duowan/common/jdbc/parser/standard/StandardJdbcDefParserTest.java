package com.duowan.common.jdbc.parser.standard;

import com.duowan.common.jdbc.model.JdbcDef;
import com.duowan.common.jdbc.parser.JdbcDefParser;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Arvin
 */
public class StandardJdbcDefParserTest {

    @Test
    public void testParser() throws Exception {

        JdbcDefParser parser = new StandardJdbcDefParser();

        Map<String, String> map = new HashMap<>();
        map.put("jdbc.std.mysql.common2.password", "admin");
        map.put("jdbc.std.mysql.common2.supportTx", "true");
        map.put("jdbc.std.mysql.common2.pool.provider", "druid");
        map.put("jdbc.std.mysql.common2.pool.dsclazz", "com.alibaba.druid.pool.DruidDataSource");
        map.put("jdbc.std.mysql.common2.jdbcUrl", "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF8");
        map.put("jdbc.std.mysql.common2.driverClass", "com.mysql.jdbc.Driver");
        map.put("jdbc.std.mysql.common2.username", "root");
        map.put("jdbc.std.mysql.common2.initJdbc", "true");
        map.put("jdbc.std.mysql.common2.proxyTx", "true");

        //map.put("jdbc.std.mysql.common.jdbcUrl", "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF8");

        List<? extends JdbcDef> defList = parser.parser(map, null);

        assertEquals(defList.size(), 1);

        JdbcDef first = defList.get(0);
        assertEquals(first.getId(), "common2");


    }
}