# yyspring-boot-starter-admincenter-ui

Admincenter 带有前端界面的UI集成， 提供以下支持：
- 集成UDB登录、退出
- 集成AdminCenter实现权限管理、权限拦截
- 集成 Vue + AdminLTE

# 使用方法
## pom 中引入
```xml
<dependency>
    <groupId>com.duowan</groupId>
    <artifactId>yyspring-boot-starter-admincenter-ui</artifactId>
</dependency>
```

## 创建相关静态资源目录
在 src/main/resources 下创建相关目录
```java_holder_method_tree
    static/admin
        views[视图页面]
        scripts
            components[组件脚本]
        tpls
```

## 配置admincenter申请的参数
```properties
yyspring.admincenter.product-id=mbga_admin
yyspring.admincenter.product-key=97MoCcsozg3kG9mow83DoDv26ikLYM

yyspring.admincenter.product-name=管理后台
yyspring.admincenter.privilege-xml-path=classpath:/static/privilege.xml
```

## 编写Controller
默认是会拦截/admin/**下的请求，所以我们如果想受到权限控制的话，需要自己让请求地址在/admin/下面

## privilege.xml
权限文件可以参考 `privilege-template.xml` 文件