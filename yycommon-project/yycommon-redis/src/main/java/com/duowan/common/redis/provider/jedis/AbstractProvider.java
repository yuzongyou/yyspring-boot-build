package com.duowan.common.redis.provider.jedis;

import com.duowan.common.redis.JedisProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * @author Arvin
 */
public abstract class AbstractProvider implements JedisProvider {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void closeResource(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }

    @Override
    public void destroyResource(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }
}
