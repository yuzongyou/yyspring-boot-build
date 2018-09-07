# 使用说明

## 引入工具包
    <dependency>
        <groupId>com.duowan</groupId>
        <artifactId>common-dns-uitil</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </dependency>
    
    <!-- 指定Maven库 -->
    <repositories>
        <repository>
            <id>releases</id>
            <url>http://devserv-game.yy.com/nexus/content/groups/public</url>
        </repository>

        <repository>
            <id>snapshots</id>
            <url>http://devserv-game.yy.com/nexus/content/groups/public-snapshots</url>
        </repository>
    </repositories>

# 基本原理
    JVM 会使用自己的DNS解析，使用的是 InetAddress， 分析这个类的源码，你可以定义自己的 NameService 并且随时更新NameService的调用链（jdk1.7+）
    
    通过把自己的NameService设置为第一优先级实现自己的DNS解析
    
    关于缓存，InetAddress 实际上自己维护了一个Cache，记录DNS的首次解析时间，每次获取的时候，计算是否过期，过期就重新走解析
    
    过期的定义：
    // check if entry has expired
    if (entry != null && policy != InetAddressCachePolicy.FOREVER) {
        if (entry.expiration >= 0 &&
            entry.expiration < System.currentTimeMillis()) {
            cache.remove(host);
            entry = null;
        }
    }
    
    expiration = System.currentTimeMillis() + (policy * 1000);
    policy 可以通过 networkaddress.cache.negative.ttl 设置，默认是缓存 30 秒 （默认 SecurityManager 是关闭的所以是 30秒，如果没关闭，more是0）
    
    securityManager: http://www.importnew.com/9751.html
    
    DNS 缓存时间： 
        networkaddress.cache.ttl
        networkaddress.cache.negative.ttl
        

