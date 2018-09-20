# Thrift 客户端自动配置支持
<font color=#0099ff size="8" face="黑体">**阅读本文档前，可以前往以下地址查看非 SpringBoot 环境下使用 Thrift 客户端的说明：**[yycommon-thrift-client](../../../yycommon-project/yycommon-thrift-client)</font>


本模块主要是集成到 SpringBoot 中使用，实现自动配置<code>Bean</code>和<code>@ThriftResource</code>支持:
1. 开发者通过配置 <code>TClientConfig</code> Bean 到 Spring 容器
2. <code>ThriftClientAutoConfiguration</code> 进行自动配置 <code>ThriftClientFactoryBean</code>
3. 业务代码中使用 <code>@ThriftResource</code> 进行对应Thrift接口的Bean注入

# 配置<code>TClientConfig</code> Bean 样例
编写 <code>ThriftClientConfigurationExample.java</code>(通常这个根据自己业务来命名，这里是示例而已):
```java
@Configuration
public class ThriftClientConfigurationExample {

    // 示例一： 服务端以 TMultiplexed + TCompact 协议同时发布多个 Thrift 服务
    @Bean
    public TClientConfig testServiceThriftClientConfig() {
        return new TClientConfig(
                new TFastFramedTransportFactory(),
                Arrays.asList(
                        new TMultiplexedCompactProtocolFactory(HiService.class, "hiService"),
                        new TMultiplexedCompactProtocolFactory(HelloService.class, "helloService")
                ),
                new FixedServerNodeDiscovery("127.0.0.1", 25000)
        );
    }

    // 示例二： 服务端以 TBinaryProtocol 发布
    @Bean
    public TClientConfig testServiceThriftClientConfig2() {
        return new TClientConfig(
                new TSocketTransportFactory(),
                new TBinaryProtocolFactory(HiService.class),
                new FixedServerNodeDiscovery("127.0.0.1", 25001)
        );
    }
}
```

假设我们有个业务类需要使用 Thrift 的服务，那么就可以使用 <code>@ThriftResource</code> 进行Bean注入：  
<code>ThriftServiceConsumer.java</code>
```java
@Component
public class ThriftServiceConsumer {
    
    @ThriftResource("hiService")
    private HiService.Iface hiService;
    
    @ThriftResource("helloService")
    private HelloService.Iface helloService;
    
    @ThriftResource
    private TestService.Iface testService;
    
    @PostConstruct
    public void testInit() {
        System.out.println(hiService.sayHi("Arvin"));
        System.out.println(helloService.sayHello("Arvin"));
        System.out.println(testService.test("Arvin"));
    }
    
}
```




