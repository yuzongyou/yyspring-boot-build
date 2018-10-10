# YY SpringBoot
结合YY实际情况的springboot启动器

# 如何deploy
如果要deploy，并且deploy同样的版本，先在最顶层的 pom.xml 中修改，即与本文件同级目录下的 pom.xml, 修改:
```xml
<version>2.0.0-SNAPSHOT</version>
和 
<yyproject.version>2.0.0-SNAPSHOT</yyproject.version>
```
为你要发布的版本，然后执行如下命令:
```cmd
mvn -N versions:update-child-modules
```
以上命令会把其他项目目录都改成统一的版本，然后你在进行 deploy：
```cmd
mvn clean deploy
```

# starters 说明
| **模块代号** | **模块说明** |
| :----------------------------------- | :-------- |
| [yyspring-boot-starter](yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter)   | 核心starter，如开发、测试、生产环境的识别，用户不同环境配置文件的加载，应用启动器等。 |
| [yyspring-boot-starter-web](yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter-web)   | web 模块启动器，支持web环境静态资源开发环境即改即现，各种视图结果等。 |
| [yyspring-boot-starter-alarm](yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter-alarm)   | 告警模块，支持logback，log4j2 直接log.error 进行告警，默认是使用logback。 |
| [yyspring-boot-starter-apollo](yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter-apollo)   | 接入apollo配置中心。 |
| [yyspring-boot-starter-innerip](yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter-innerip)   | 内部IP查询，如办公网IP查询。 |
| [yyspring-boot-starter-ipowner](yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter-ipowner)   | IP归属查询服务，如IP归属省、市、县。 |
| [yyspring-boot-starter-timer](yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter-timer)   | 定时器任务。 |
| [yyspring-boot-starter-virtualdns](yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter-virtualdns)   | 虚拟DNS，避免配置各种HOST。 |
| [yyspring-boot-starter-recachedns](yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter-recachedns)   | DNS 自动刷新缓存，使用过的域名则一直可以从缓存中获取并且异步更新缓存。 |
| [yyspring-boot-starter-udb-security](yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter-udb-security)   | UDB 登录鉴权，支持UDB登录验证相关的页面参数。 |
| [yyspring-boot-starter-admincenter](yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter-admincenter)   | 接入权限管理中心，不包含UI。 |
| [yyspring-boot-starter-admincenter-ui](yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter-admincenter-ui)   | 接入权限管理中心，包含UI。 |
| [yyspring-boot-starter-jdbc](yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter-jdbc)   | JDBC 支持(包括升龙的和标准数据源)，支持多数据源。 |
| [yyspring-boot-starter-redis](yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter-redis)   | Redis 支持(包括升龙、标准Redis和哨兵redis)，支持多数据源。 |
| [yyspring-boot-starter-thrift-client](yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter-thrift-client)   | Thrift Client 支持，支持连接池、监控等。 |
| [yyspring-boot-starter-thrift-server](yyspring-boot-project/yyspring-boot-starters/yyspring-boot-starter-thrift-server)   | Thrift Server 支持，支持同时发布多个 Thrift 服务到一个端口以及多种发布方式。 |


# 其他模块说明
| **模块代号** | **模块说明** |
| :----------------------- | :-------- |
| [yyspring-boot](yyspring-boot-project/yyspring-boot)   | 应用程序启动核心引导，处理环境，资源加载等。 |
| [yyspring-autoconfigure](yyspring-boot-project/yyspring-boot-autoconfigure)   | 内部自动配置，集成内部常用组件的自动配置。 |
| [yyproject-bom](yyproject-bom)   | 组件依赖，管理各种版本问题, 开发的时候直接设置这个为parent或者 import pom 即可。 |

# 示例项目说明
| **示例代号** | **模块说明** |
| :----------------------- | :-------- |
| [yyspring-boot-udb-security-samples](yyspring-boot-samples/yyspring-boot-udb-security-samples)   | 接口进行UDB登录态拦截web项目示例。 |


# 附录
[GitLab Markdown 语法参考指南](https://docs.gitlab.com/ee/user/markdown.html)
