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
   - 自动注册Bean，只要能获取到 BeanDefinitionRegistry 就能进行注册， 如果是SpringBoot这可以自动配置
   - 拦截切面，使用 ProxyFactoryBean addAdvice
   - 连接池管理 commons-pool2
   - 负载均衡，提供接口，使用 SPI 实现负载均衡算法实现
    



