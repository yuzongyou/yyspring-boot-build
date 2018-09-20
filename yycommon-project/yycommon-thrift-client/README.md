# thrift client support
Thrift Client 支持，连接池实现，自定义连接池对象验证，调用拦截器，重试，超时等功能

# 基本概念说明
## serviceClass
serviceClass 指的是通过 Thrift IDL 文件生成的 Java 类，该类内部又定义了很多 Iface，AsyncIface，Client，AsyncClient 内部类，通常我们使用的时候就是创建了 Client， AsyncClient 对象；  
本模块主要实现了 Client、Iface 接口类的实现并*按照*接口进行注入，AsyncIface、AsyncClient 后续会增加支持。

## ThriftServerNode
Thrift 服务端节点抽象，包含节点主机地址、端口，通常提供一个IP:PORT

## ThriftInterceptor
Thrift 方法调用拦截器，允许你进行扩展，提供三个方法，包括 *before()*, *afterReturning()*, *afterThrowing()*, 开发者可以选择合适的业务进行自己定制，比如：  
- 拦截日志，默认有 ThriftLogginInterceptor， 如果你不喜欢这个实现可以自己设置
- 方法耗时拦截
- 其他方面的拦截

## ServerNodeProvider
Thrift 服务节点提供者，充当服务节点发现的功能，你可以自由实现，比如集成到 Eureka，Consul，或者自己定义，本模块提供的是固定节点的提供者实现： FixServerNodeProvider  
这个类适用于服务节点不变化或者几乎不会发生变化的场景，亦或者是使用域名 + 端口的形式

## LoadBalancer
客户端负载均衡器，默认是使用轮询的方式，通过这个接口来获取 *ThriftServerNode* 对象，然后将这个节点交给连接池进行TTransport的创建以及相关Client对象的创建  

默认的实现是 *DefaultLoadBalancer* 类，该类需要一个 *ServerNodeProvider* 接口实现类来提供全量的服务节点

## ClientValidator
客户端对象有效性验证，提供给连接池使用，连接池会有一个空闲检查功能，当池中对象空闲一定时间（默认 30 秒就会调用这个方法来检测这个客户端连接对象的有效性）

## ClientType
客户端对象类型，有两种： Iface（同步）， AsyncIface（异步），目前仅仅支持 Iface 的同步类型

## TClientPoolConfig
Thrift 客户端连接池配置对象，继承于 commons2-pool

## TProtocolFactory
TProtocol 工厂接口，这个非常重要，在接入 Thrift 服务的时候，首先需要了解服务端的发布形式，要了解服务端采用的是什么协议方式发布的，比如服务端可能直接使用TBinaryProtocol 协议发布，那么客户端也应该使用对应的协议，否则调用不到具体接口  

在本模块中提供了几个工厂方法：

| **TProtocolFactory** | **说明** |
| :--- | :------ |
| TBinaryProtocolFactory | 主要针对服务端以 TBinaryProtocol 协议发布的服务 |
| TCompactProtocolFactory | 主要针对服务端以 TCompactProtocol 协议发布的服务 |
| TMultiplexedBinaryProtocolFactory | 主要针对服务端采用 TMultiplexed + TBinaryProtocol 方式发布的服务 |
| TMultiplexedCompactProtocolFactory | 主要针对服务端采用 TMultiplexed + TCompactProtocol 方式发布的服务 |

## TTransportFactory
TTransport 工厂接口，这个也很重要，在接入 Thrift 服务的时候，首先需要了解服务端的发布形式，要了解服务端采用的是什么工厂方式创建的 TTransport，客户端采用对应的方式创建才行。  

在本模块中提供了几个工厂方法：

| **TTransportFactory** | **说明** |
| :--- | :------ |
| TSocketTransportFactory | 对应服务端以默认 org.apache.thrift.transport.TTransportFactory 方式发布的服务 |
| TFramedTransportFactory | 对应服务端以默认 org.apache.thrift.transport.TFastFramedTransport.Factory 方式发布的服务 |
| TFastFramedTransportFactory | 对应服务端以默认 org.apache.thrift.transport.TFramedTransport.Factory 方式发布的服务 |

# How To Use
本小节主要讲解如何使用的问题，流程基本都是一样的，要接入 Thrift 服务的一般流程是：
1. 从 Thrift 服务提供方获取 Thrift 定义文件
2. 咨询或阅读 Thrift 服务提供方，了解 Thrift 服务发布的形式，主要是了解服务端 TTransportFactory 和 TProtocol 是什么
3. 根据 Thrift 服务端的发布协议，定制客户端的连接协议
4. 确定本服务的业务需求，比如监控、调用拦截等等
5. 配置 TClientConfig Bean 对象
6. 配置Bean com.duowan.common.thrift.client.ThriftResourceBeanPostProcessor， 用于实现 @ThriftResource 自动注入 Thrift 服务
7. 测试

大体是上面的流程，下面将举例子来实际说明这个过程， 包含各种形式的Thrift服务发布和客户端配置

## TestService.thrift IDL 服务定义
为了方便演示，我们从 编写 Thrift 协议文件开始，假设我们有如下协议文件：  
HiService.thrift:
```thrift
/**
* 定义包名
**/
namespace java com.duowan.thrift.service

/**
* 服务接口, 仅测试用
**/
service HiService {

    /**
	* 简单的连接测试
	*/
	void ping();

	/**
	* @param name 名称
	**/
	string sayHi(1:string name);
}
```

HelloService.thrift:
```thrift
/**
* 定义包名
**/
namespace java com.duowan.thrift.service

/**
* 服务接口, 仅测试用
**/
service HelloService {

    /**
	* 简单的连接测试
	*/
	void ping();

	/**
	* @param name 名称
	**/
	string sayHhello(1:string name);
}
```

## 根据 Thrift IDL 文件生成 java 代码
thrift 下载地址：
```shell
wget http://archive.apache.org/dist/thrift/0.10.0/thrift-0.10.0.exe
```

cd 到thrift的文件目录，然后执行以下命令
```cmd
thrift-0.10.0.exe -gen java HiService.thrift
thrift-0.10.0.exe -gen java HelloService.thrift
```

然后就会在相应的目录上生成对应的 java 代码, 本样例生成代码如下：
```cmd
└─gen-java
    └─com
        └─duowan
            └─thrift
                └─service
                        HiService.java
                        HelloService.java
```
然后你需要把这个生成的文件拷贝到执行的包路径下，如上图所示，应该把 *HiService.java*,*HelloService.java* 拷贝到 *com.duowan.thrift.service* 包下。

## 编写实现类
实现接口 *HiService.Iface* 和 *HelloService.Iface*, 如下两个类：  
**HiServiceImpl**:
```java
public class HiServiceImpl implements HiService.Iface {
    @Override
    public void ping() throws TException { }

    @Override
    public String sayHi(String name) throws TException {
        return "Hi, " + name;
    }
}
```
**HelloServiceImpl**:
```java
public class HelloServiceImpl implements HelloService.Iface {
    @Override
    public void ping() throws TException { }

    @Override
    public String sayHello(String name) throws TException {
        return "Hello, " + name;
    }
}
```

## 发布 Thrift 服务与客户端配置对照说明

### 线程池 + 单个服务 + 默认TTransportFactory
服务发布代码如下：  

```java
public static void main(String[] args) throws Exception {
    singlePublishBySingle(25000);
}
public static void singlePublishBySingle(int port) throws Exception {
    TServerSocket serverTransport = new TServerSocket(port);
    TBinaryProtocol.Factory portFactory = new TBinaryProtocol.Factory(true, true);
    TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
    HiService.Processor<HiService.Iface> processor = new TestService.Processor<HiService.Iface>(new HiServiceImpl());

    args.processor(processor);
    args.protocolFactory(portFactory);
    TServer server = new TThreadPoolServer(args);
    server.serve();
}
```

**对应客户端代码使用方式一：**  
```java
public static void main(String[] args) {
    ThriftServerNode serverNode = new ThriftServerNode("127.0.0.1", 25000);
    ServerNodeProvider serverNodeProvider = new FixedServerNodeProvider(Collections.singletonList(serverNode));
    
    // 上述这种发布形式，应该使用 TSocketTransportFactory 和 TBinaryProtocolFactory
    TClientConfig clientConfig = new TClientConfig(new TSocketTransportFactory(), new TBinaryProtocolFactory(), serverNodeProvider);
    
    ThriftClientFactoryBean factoryBean = new ThriftClientFactoryBean(clientConfig, null, ClientType.IFACE);
    // 注意这里只能使用 接口
    HiService.Iface hiService = (HiService.Iface) factoryBean.getObject();
    
    assertEquals(hiService.sayHi("Arvin"), "Hi, Arvin");
}
```
**对应客户端代码使用方式二：**
直接使用 Spring XML 来描述，这个可能显得更加复杂一点：
applicationContext.xml:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="transportFactory" class="com.duowan.common.thrift.client.factory.transport.TSocketTransportFactory"/>
    <bean id="serverNodeProvider" class="com.duowan.common.thrift.client.servernode.FixedServerNodeProvider">
        <constructor-arg>
            <list>
                <bean class="com.duowan.common.thrift.client.config.ThriftServerNode">
                    <constructor-arg index="0" value="127.0.0.1"/>
                    <constructor-arg index="1" value="25000"/>
                </bean>
            </list>
        </constructor-arg>
    </bean>

    <bean id="clientConfig" class="com.duowan.common.thrift.client.config.TClientConfig">
        <constructor-arg index="0" ref="transportFactory"/>
        <constructor-arg index="1">
            <list>
                <bean class="com.duowan.common.thrift.client.factory.protocol.TBinaryProtocolFactory">
                    <constructor-arg index="0" value="com.duowan.thrift.service.HiService"/>
                </bean>
            </list>
        </constructor-arg>
        <constructor-arg index="2" ref="serverNodeProvider"/>
    </bean>

    <bean id="hiServiceIface" class="com.duowan.common.thrift.client.factory.ThriftClientFactoryBean">
        <constructor-arg index="0" ref="clientConfig"/>
    </bean>
    <!-- @ThriftResource 处理类，这个Bean可以不需要，如果你不需要使用 @ThriftResource 的话 -->
    <bean class="com.duowan.common.thrift.client.ThriftResourceBeanPostProcessor"/>

</beans>
```
对应的 Java 测试代码如下：  
```java
@Test
public void testInitByApplicationContext2() throws TException, InterruptedException {
    ApplicationContext acx = new ClassPathXmlApplicationContext("classpath:/applicationContext.xml");

    HiService.Iface hiService = acx.getBean(HiService.Iface.class);
    //HiService.Iface hiService = acx.getBean("hiServiceIface", HiService.Iface.class);

    assertEquals(hiService.sayHi("Arvin"), "Hi, Arvin");
}
```

### 线程池 + 一个端口同时发布多个服务
服务发布代码如下：  

```java
public static void main(String[] args) throws Exception {
    singlePublishByRouter(25000, "hiService", "helloService");
}
public static void singlePublishByRouter(int port, String hiServiceRouter, String helloServiceRouter) throws Exception {
    TServerSocket serverTransport = new TServerSocket(port);
    TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
    
    // 为了演示，这里采用不同的 Transport 和 TProtocol 工厂对象， 这一这里的不同会导致客户端的使用方式也不同
    args.transportFactory(new TFastFramedTransport.Factory());
    args.protocolFactory(new TCompactProtocol.Factory());
    
    // 同一个端口上发布两个服务，需要使用到 TMul
    HiService.Processor<HiService.Iface> hiProcessor = new TestService.Processor<HiService.Iface>(new HiServiceImpl());
    HelloService.Processor<HelloService.Iface> helloProcessor = new HelloService.Processor<HelloService.Iface>(new HelloServiceImpl());
    
    TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();
    multiplexedProcessor.registerProcessor(hiServiceRouter, hiProcessor);
    multiplexedProcessor.registerProcessor(helloServiceRouter, helloProcessor);

    args.processor(multiplexedProcessor);
    
    TServer server = new TThreadPoolServer(args);
    server.serve();
}
```

**对应客户端代码使用方式一：**  
```java
public static void main(String[] args) {
    ThriftServerNode serverNode = new ThriftServerNode("127.0.0.1", 25000);
    ServerNodeProvider serverNodeProvider = new FixedServerNodeProvider(Collections.singletonList(serverNode));
    
    // 上述这种发布形式，应该使用 TFastFramedTransportFactory 和 TMultiplexedCompactProtocolFactory
    TClientConfig clientConfig = new TClientConfig(
            new TFastFramedTransportFactory(),  // 因为服务端使用的是 TFastFramedTransport.Factory
            Arrays.asList( // 因为服务端采用了 TMultiplexedProcessor 来包装 TCompactProtocol
                    new TMultiplexedCompactProtocolFactory(HiService.class, "hiService"),
                    new TMultiplexedCompactProtocolFactory(HelloService.class, "helloService")
            ),
            serverNodeProvider);
    
    ThriftClientFactoryBean hiServiceFactoryBean = new ThriftClientFactoryBean(clientConfig, "hiService", ClientType.IFACE);
    ThriftClientFactoryBean helloServiceFactoryBean = new ThriftClientFactoryBean(clientConfig, "helloService", ClientType.IFACE);
    
    // 注意这里只能使用 接口
    HiService.Iface hiService = (HiService.Iface) hiServiceFactoryBean.getObject();
    HelloService.Iface helloService = (HelloService.Iface) helloServiceFactoryBean.getObject();
    
    assertEquals(hiService.sayHi("Arvin"), "Hi, Arvin");
    assertEquals(helloService.sayHello("Arvin"), "Hello, Arvin");
}
```

**对应客户端代码使用方式二：**
直接使用 Spring XML 来描述，这个可能显得更加复杂一点：
applicationContext.xml:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="fastFramedTransportFactory" class="com.duowan.common.thrift.client.factory.transport.TFastFramedTransportFactory"/>

    <bean id="serverNodeProvider" class="com.duowan.common.thrift.client.servernode.FixedServerNodeProvider">
        <constructor-arg index="0" value="127.0.0.1"/>
        <constructor-arg index="1" value="25000"/>
    </bean>

    <bean id="clientConfig" class="com.duowan.common.thrift.client.config.TClientConfig">
        <constructor-arg index="0" ref="fastFramedTransportFactory"/>
        <constructor-arg index="1">
            <list>
                <bean class="com.duowan.common.thrift.client.factory.protocol.TMultiplexedCompactProtocolFactory">
                    <constructor-arg index="0" value="com.duowan.thrift.service.HiService"/>
                    <constructor-arg index="1" value="hiService"/>
                </bean>
                <bean class="com.duowan.common.thrift.client.factory.protocol.TMultiplexedCompactProtocolFactory">
                    <constructor-arg index="0" value="com.duowan.thrift.service.HelloService"/>
                    <constructor-arg index="1" value="helloService"/>
                </bean>
            </list>
        </constructor-arg>
        <constructor-arg index="2" ref="serverNodeProvider"/>
    </bean>

    <bean id="hiService" class="com.duowan.common.thrift.client.factory.ThriftClientFactoryBean">
        <constructor-arg index="0" ref="clientConfig"/>
        <constructor-arg index="1" value="hiService"/>
        <constructor-arg index="2" value="IFACE"/>
    </bean>

    <bean id="helloService" class="com.duowan.common.thrift.client.factory.ThriftClientFactoryBean">
        <constructor-arg index="0" ref="clientConfig"/>
        <constructor-arg index="1" value="helloService"/>
        <constructor-arg index="2" value="IFACE"/>
    </bean>

    <!-- @ThriftResource 处理类，这个Bean可以不需要，如果你不需要使用 @ThriftResource 的话 -->
    <bean class="com.duowan.common.thrift.client.ThriftResourceBeanPostProcessor"/>

</beans>
```

对应的 Java 测试代码如下：  
```java
@Test
public void testInitByApplicationContext2() throws TException, InterruptedException {
    ApplicationContext acx = new ClassPathXmlApplicationContext("classpath:/applicationContext.xml");

    HiService.Iface hiService = acx.getBean(HiService.Iface.class);
    //HiService.Iface hiService = acx.getBean("hiServiceIface", HiService.Iface.class);

    assertEquals(hiService.sayHi("Arvin"), "Hi, Arvin");
    
    HelloService.Iface hiService = acx.getBean(HelloService.Iface.class);
    //HelloService.Iface helloService = acx.getBean("helloService", HelloService.Iface.class);
    
    assertEquals(helloService.sayHello("Arvin"), "Hello, Arvin");
}
```
或者使用 JUnit 测试：
```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/applicationContext.xml"})
public class ThriftXmlTest2 {

    @ThriftResource("hiService")
    private HiService.Iface hiService;

    @ThriftResource("helloService")
    private HelloService.Iface helloService;

    @Test
    public void test() throws TException {
        assertEquals(helloService.sayHello("Arvin"), "Hello, Arvin");
        assertEquals(hiService.sayHi("Arvin"), "Hi, Arvin");
    }
}
```












