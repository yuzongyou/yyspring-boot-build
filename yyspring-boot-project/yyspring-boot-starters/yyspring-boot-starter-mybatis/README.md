# mybatis 自动配置
基于 jdbc， 支持单、多数据源

## 示例代码
[yyspring-boot-mybatis-samples](../../../yyspring-boot-samples/yyspring-boot-mybatis-samples)

## 开发流程
### <code>pom.xml</code>引入依赖
```xml
<dependency>
    <groupId>com.duowan</groupId>
    <artifactId>yyspring-boot-starter-mybatis</artifactId>
</dependency>
```

### <code>application.properties</code> 配置 <code>Jdbc</code> 数据源
```properties
# 定义标准数据源
yyspring.jdbc.standards.common.jdbcUrl=jdbc:mysql://${host}:${port}/${schema}?useUnicode=true&characterEncoding=UTF8
yyspring.jdbc.standards.common.username=${username}
yyspring.jdbc.standards.common.password=${password}

# 定义 mybatis 定义源
yyspring.mybatis.sources.common.mapper-scan.base-package-classes=com.duowan.mybatis.dao.mybatis.SampleMapper
yyspring.mybatis.sources.common.config-location=classpath:mybatis/mybatis-config.xml
yyspring.mybatis.sources.common.mapper-locations=classpath:mybatis/*Mapper.xml
```

### 关于 <code>mybatis-config.xml</code>
该文件是可选的，可以不写，如果你是纯注解开发的，那么只需要配置一下 <code>Mapper</code> 接口的扫描包或基类就可以了

### 关于 <code>*Mapper.xml</code>
这个配置也是可选的，如果你是纯注解开发的，那么这个都不用配置，当然，也可以是混合的，接口中一部分直接使用注解实现，另一部分基于 <code>Mapper.xml</code> 实现

### 一个 <code>Xml</code> 和 <code>Annotation</code> 混合使用的 <code>Mapper</code> 接口实例
```java
package com.duowan.mybatis.dao.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface SampleMapper {

    @Select("show databases")
    List<String> getDatabases();

    @Select("show tables")
    List<String> getTables();

    /**
     * 这个基于 Mapper.xml 实现
     */
    String selectSelf(@Param("self") String self);
}
```

其中， <code>getDatabases</code> 和 <code>getTables</code> 使用 <code>Annotation</code> 形式实现；而 <code>selectSelf</code> 则使用 <code>Xml</code> 方式实现

上述对应的 <code>SampleMapper.xml</code> 为：
```xml
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.duowan.mybatis.dao.mybatis.SampleMapper">
    <select id="selectSelf" resultType="java.lang.String">
        select #{self} from dual
    </select>
</mapper>
```

## 多数据源开发说明
如果是多数据源的话，建议将不同数据源的对应的 <code>Mapper</code> 接口和 <code>XML</code> 文件分开包、目录进行存储，一个数据源对应一个包。

假设我们有两个数据源， common, user, 推荐创建如下目录结构：

|--com/duowan/xxx/mapper/  
|-----------------------/common/  
|------------------------------/XxxxMapper.java  
|-----------------------/user/  
|------------------------------/YyyyMapper.java  

|--src/main/resources/  
|--------------------/mapper/   
|---------------------------/common/  
|----------------------------------/mybatis-config.xml  
|----------------------------------/XxxxMapper.xml  
|---------------------------/user/  
|--------------------------------/mybatis-config.xml  
|--------------------------------/YyyyMapper.xml  

配置 mybatis 的时候，配置类似如下：
```properties
# 定义 common 数据源的 mybatis 配置
yyspring.mybatis.sources.common.mapper-scan.base-package-classes=com.duowan.xxx.mapper.common.XxxxMapper
yyspring.mybatis.sources.common.config-location=classpath:mapper/common/mybatis-config.xml
yyspring.mybatis.sources.common.mapper-locations=classpath:mybatis/common/*Mapper.xml

# 定义 user 数据源的 mybatis 配置
yyspring.mybatis.sources.user.mapper-scan.base-package-classes=com.duowan.xxx.mapper.user.YyyyMapper
yyspring.mybatis.sources.user.config-location=classpath:mapper/user/mybatis-config.xml
yyspring.mybatis.sources.user.mapper-locations=classpath:mybatis/user/*Mapper.xml
```








