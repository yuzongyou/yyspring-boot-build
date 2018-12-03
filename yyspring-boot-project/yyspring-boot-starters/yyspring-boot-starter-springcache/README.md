## 启动SpringCache
    
    # redis的基本配置基于 yyspring-boot-starter-redis
   配置详情参见： [Redis配置](https://git.yy.com/opensource/yygame/yyspring-boot/tree/master/yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter-redis)

### 集成starter

            <dependency>
                <groupId>com.duowan</groupId>
                <artifactId>yyspring-boot-starter-springcache</artifactId>
            </dependency>

### 配置与使用

    # 配置缓存的redis实例，可以多个，默认会注册{xxx}RedisCacheManager
    yyspring.redis.cache-ids=common,user
    
    # 指定主的缓存实例，多个时需指定
    yyspring.redis.primary-cache-id=common
    
    # 配置全局的ttl，针对所有cacheName有效(单位秒)
    yyspring.redis.cache-expired-time=300
    
    # 代码使用
    # cacheName为user#86400,可以单独为user这个cacheName设置ttl，#号后面的为过期时间，单位秒
    # 配置多个cacheManager时,可以进行指定
    
    @Repository
    @CacheConfig(cacheNames = UserDaoMysqlImpl.redisKey, cacheManager = "commonRedisCacheManager")
    public class UserDaoMysqlImpl implements UserDao {
        public static final String redisKey = "user#86400";
     
         @Override
         @CacheEvict(key = "#userId")
         public Integer updateStatus(String userId, Integer status) {
            
         }   
    }