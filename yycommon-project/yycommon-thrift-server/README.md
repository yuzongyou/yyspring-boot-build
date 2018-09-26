# Thrift Service Publish
<code>Thrift</code> 服务发布服务端，支持多种服务发布形式，包括：  

1. 单线程阻塞式
2. 单线程非阻塞式
3. 线程池阻塞式
4. 半同步半异步
5. 线程池选择器

开发者可以根据实际情况来选择具体的方式进行发布

# 发布一个Thrift服务流程
要发布一个<code>Thrift</code> 服务，我们需要进行至少以下几种设置：  

1. 选择以什么形式发布，如上一节中的发布方式
2. 选择要多个服务发布到一个端口还是一个服务单独发布一个端口
3. 确定服务的发布协议，包括 <code>TTransport</code>、<code>TProtocol</code> 工厂应该使用什么
4. 确定额外的发布参数，如发布端口、路由地址、连接池参数等等
5. 如何发布？ 本模块支持 <code>applicationContext.xml</code> 方式配置并发布，也支持注解形式发布

# 非 <code>SpringBoot</code> 中使用
## TestService.thrift IDL 服务定义
为了方便演示，我们从 编写 Thrift 协议文件开始，假设我们有如下协议文件：  
<code>HiService.thrift</code>:
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

<code>HelloService.thrift</code>:
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
实现接口 <code>*HiService.Iface*</code> 和 <code>*HelloService.Iface*</code>, 如下两个类：  
<code>**HiServiceImpl**:</code>
```java
@ThriftService
public class HiServiceImpl implements HiService.Iface {
    @Override
    public void ping() throws TException { }

    @Override
    public String sayHi(String name) throws TException {
        return "Hi, " + name;
    }
}
```
<code>**HelloServiceImpl**:</code>
```java
@ThriftService
public class HelloServiceImpl implements HelloService.Iface {
    @Override
    public void ping() throws TException { }

    @Override
    public String sayHello(String name) throws TException {
        return "Hello, " + name;
    }
}
```

## 在即将发布的服务类上使用 <code>@ThriftService</code>
这个主要是用来标识一个 Thrift 服务，非常重要，只有使用了这个注解的才会被发布（当然你可以自己定义ThriftServiceExporter）

## 编写 Spring 配置文件
要启用自动发布功能，需要引入一个文件<code>applicationContext-thrift-exporter.xml</code>

比如有一个测试的文件：
<code>**applicationContext-test1.xml**</code>:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>
    <import resource="applicationContext-thrift-exporter.xml"/>
    <context:component-scan base-package="com.duowan.thrift.service.impl"/>
</beans>
```

然后就可以启动项目发布 Thrift 服务了


# 自定义发布器
默认情况下，如果你没有自己配置 <code>ThriftServiceExporter</code>, 那么会自动配置一个默认的  <code>TThreadPoolServerExporter</code> 用于发布 Thrift 服务。

默认情况下发布的端口是 *12000*

因此，如果你想自己定义发布的端口，或者其他配置，可以自己在你的 Spring XML 文件中配置 <code>ThriftServiceExporter</code> 的参数，本模块默认提供了几个 <code>ThriftServiceExporter</code> 的实现，介绍如下：

| **ThriftServiceExporter** | **说明** |
| :--- | :------ |
| <code>TThreadPoolServerExporter</code> | 默认的 Thrift 服务发布器，对应 TThreadPoolServer |
| <code>THsHaServerExporter</code> | 对应 THsHaServer |
| <code>TNonblockingServerExporter</code> | 对应 TNonblockingServer |
| <code>TThreadedSelectorServerExporter</code> | 对应 TThreadedSelectorServer |
| <code>TSimpleServerExporter</code> | 对应 TSimpleServer， 这个极度不推荐使用，尽量是不用用的 |

## 自定义发布器示例

自定义 <code>TThreadPoolServerExporter</code>:
```xml
<bean id="thriftServiceExporter" class="com.duowan.common.thrift.server.exporter.TThreadPoolServerExporter">
    <!-- 否加入到主线程中去, 即和主线程同一个线程进行监听 -->
    <property name="joinToParentThread" value="true"/>
    <!-- 最大工作线程数，默认是 处理器数量 * 5 -->
    <property name="maxWorkerThreads" value="20"/>
    <!-- 最小工作线程数，默认是 5 -->
    <property name="minWorkerThreads" value="5"/>
    <!-- Thrift 服务发布的监听端口 -->
    <property name="port" value="12580"/>
    <!-- 连接和读超时时间设置 -->
    <property name="requestTimeoutMillis" value="3000"/>
</bean>
```

其他的可以参考具体类的参数来设置，如果你想实现更加复杂的发布策略，可以自己实现接口<code>com.duowan.common.thrift.server.exporter.ThriftServiceExporter</code>

或者直接继承抽象类： <code>com.duowan.common.thrift.server.exporter.AbstractThriftServiceExporter</code>









