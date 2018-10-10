# 具有UDB校验的web项目示例

## 场景说明
- 希望某个地址下都要进行UDB登录态校验
- 假设某个地址下(如 /admin/**) 要求登录态，但是希望其下某些不需要拦截
- 没有配置拦截地址，在具体某个接口上决定是否需要拦截

## 如何使用？

### 创建一个标准的 Maven项目
用你熟悉的工具创建一个Maven项目，（如 Eclipse,IDEA 等）

### 修改pom.xml,引入相关依赖
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!-- 引入父级 POM -->
    <parent>
        <groupId>com.duowan</groupId>
        <artifactId>yyproject-bom</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>yyspring-boot-udb-security-samples</artifactId>

    <dependencies>
        <!-- 添加 yyspring-boot-starter-udb-security 依赖 -->
        <dependency>
            <groupId>com.duowan</groupId>
            <artifactId>yyspring-boot-starter-udb-security</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- 加上这个插件就可以直接通过 mvn spring-boot:run 来运行程序了， 同时会打包成 springboot 特有的fat jar包 -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

        </plugins>
    </build>

</project>
```

当然，如果你不想把 yyproject-bom 作为父级，也可以在依赖管理包中引入，可以完全用下面这个样子替代上面这个配置：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.duowan</groupId>
    <artifactId>yyspring-boot-udb-security-samples</artifactId>
    <version>1.0.0-SNAPSHOT</version>

    <dependencies>
        <!-- 添加 yyspring-boot-starter-udb-security 依赖 -->
        <dependency>
            <groupId>com.duowan</groupId>
            <artifactId>yyspring-boot-starter-udb-security</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.duowan</groupId>
                <artifactId>yyproject-bom</artifactId>
                <version>2.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <!-- 加上这个插件就可以直接通过 mvn spring-boot:run 来运行程序了， 同时会打包成 springboot 特有的fat jar包 -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

        </plugins>
    </build>

</project>
```

当然，我们是强烈建议使用第一种形式的

### 创建SpringBoot启动类
```java

import com.duowan.yyspring.boot.annotations.YYSpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@YYSpringBootApplication(moduleNo = "udbsecure")
public class UdbSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(UdbSecurityApplication.class, args);
    }
}
```

### 创建 Controller
这里就不多说了，可以看本实例项目的代码，启动本项目看看具体的请求拦截情况


### 关于 UdbSecurity 的配置
可以配置的项目在类 <code>com.duowan.yyspringboot.autoconfigure.udbsecurity.UdbSecurityProperties</code> 中定义好了，前缀都是 <code>yyspring.udbsecurity</code>
```properties
# 定义要拦截的地址
yyspring.udbsecurity.path-patterns=/admin/**,/secure/**,/mgr/**

# 定义不需要拦截的地址，注意，不拦截的会先生效，拦击于不拦截的都有的话，那么不拦截
yyspring.udbsecurity.exclude-path-patterns=/mgr/**

```

### 注解说明
- <code>@UdbCheck</code> 可以在 Controller 类或该类的方法 中使用这个注解，表示需要进行UDB登录态校验
- <code>@IgnoredUdbCheck</code> 可以在 Controller 类或该类的方法 中使用这个注解，表示不需要进行UDB登录态校验

### Controller 自动参数注入
参考 <code>UdbController</code> 类