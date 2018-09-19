# thrift client support
Thrift Client 支持

# 设计思路
1. 抽象出以下概念  
   - ThriftServerNode Thrift 服务提供者节点， 主要包含服务主机地址，IP
   - ThriftLoadBalancer 客户端负载均衡器, 选择一个可用的节点，服务节点监控等
   - TTransportFactory 工厂， 用于生产 TTransport 对象
   - TServiceClientFactory 工厂，生产最终用于调用Thrift服务的实例对象，此处涉及到 TTransport，TProtocol 的构建问题
   - ThriftInterceptor 调用切面拦截器，允许开发者自己织入，比如调用耗时，参数打印，日志输出等
   
2. ThriftClientConfig -- 客户端配置，包含删除抽象概念的各种配置，然后构造各种bean
  
3. 关键点  
   - 自动注册Bean，只要能获取到 BeanDefinitionRegistry 就能进行注册， 如果是 SpringBoot 则可以自动配置
   - 拦截切面，使用 ProxyFactoryBean addAdvice
   - 连接池管理 commons-pool2
   - 负载均衡，提供接口，使用 SPI 实现负载均衡算法实现
    

```text
{
    Class<?> serviceClass:      REQUIRED, Thrift IDL 生成的服务类
    TTransportFactory:          REQUIRED, 服务发布方，一个端口只能发布一种
    List<TProtocolFactory>:     REQUIRED, 客户端服务路由，如果服务端以TMultiplexedProcessor发布，那么可以有多个，每一个对应一个具体服务
                                如果有多个Factory的情况, 不允许出现 router() 为空的情况，并且不允许 router() 有重复的
    
    LoadBalancer:               OPTIONAL, 负载均衡器，如果没有自定义则使用默认的负载均衡器，如果没有自定义，那么 ServerNodeProvider 不允许为空
    ServerNodeProvider:         OPTIONAL, Thrift 服务端节点提供者（可以不提供，如果提供了自定义的 LoadBalancer 则可以为空）
    
    timeoutMillis:        OPTIONAL, default=5000, 连接超时时间，单位毫秒
    TClientPoolConfig:          OPTIONAL, 连接池配置, 无则使用默认的
    List<ThriftInterceptor>:    OPTIONAL, 调用拦截器
    NodeMonitor:                OPTIONAL, 节点监控对象，默认是使用 telnet host:port 监控
    MonitorReporter:            OPTIONAL, 监控报告接口，默认是直接打印日志
    defaultRetryTimes:          OPTIONAL, 重试次数，默认是重试一次，如果ServiceRouter没有设置则以这个为准
}
```

注入的时候，需要明确知道注入的是什么：
1. 已知接口，知道注入的类型
2. 注入什么对象？ 如果服务端是 TMultiplexedProcessor ， 那么就需要注入 router 相同的，这就需要自定义注入的逻辑
   可以采用 @Resource 注入， 根据 Router 名称来作为Bean的后缀，需要注册多个 Bean

spring 源码：  
org.springframework.core.io.support.SpringFactoriesLoader.loadFactories
