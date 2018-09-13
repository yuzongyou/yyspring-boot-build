package com.duowan.common.redis.parser.standard;

import com.duowan.common.redis.model.RedisDef;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author Arvin
 */
public class StandardRedisDefParserTest {

    @Test
    public void testParseRedisDefList() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("redis.std.common.server", "localhost:6379");
        map.put("redis.std.common.pool.maxActive", "128");
        map.put("redis.std.common.password", "123456");
        map.put("redis.std.common.timeout", "3000");
        map.put("redis.std.common.db", "1");

        StandardRedisDefParser parser = new StandardRedisDefParser();

        List<? extends RedisDef> redisDefList = parser.parseRedisDefList(map, null);

        assertTrue(redisDefList != null && !redisDefList.isEmpty());
    }
}