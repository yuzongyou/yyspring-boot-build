package com.duowan.common.redis.provider.jedis;

import com.duowan.common.redis.JedisProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;

/**
 * @author Arvin
 */
public abstract class AbstractProvider implements JedisProvider {

    protected final Log logger = LogFactory.getLog(getClass());

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
