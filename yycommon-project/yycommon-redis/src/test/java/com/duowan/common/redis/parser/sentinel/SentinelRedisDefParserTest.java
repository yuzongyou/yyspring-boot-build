package com.duowan.common.redis.parser.sentinel;

import com.duowan.common.redis.model.RedisDef;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Arvin
 */
public class SentinelRedisDefParserTest {

    @Test
    public void testParseRedisDefList() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("redis.sentinel.common.sentinels", "localhost:6379");
        map.put("redis.sentinel.common.masterName", "hello");
        map.put("redis.sentinel.common.pool.maxActive", "128");
        map.put("redis.sentinel.common.password", "123456");
        map.put("redis.sentinel.common.timeout", "3000");
        map.put("redis.sentinel.common.db", "1");

        SentinelRedisDefParser parser = new SentinelRedisDefParser();

        List<? extends RedisDef> redisDefList = parser.parseRedisDefList(map, null);

        assertTrue(redisDefList != null && !redisDefList.isEmpty());
    }
}