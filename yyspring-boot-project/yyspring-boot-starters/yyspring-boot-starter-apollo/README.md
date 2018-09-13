# 使用Apollo配置中心
自动识别 DWPROJECTNO 作为 Apollo 配置中心的项目代号， 直接在对应环境中配置 Apollo 配置项，在原有的配置项中，不存在 apollo的增加 apollo 前缀

如你想自定义 apollo 的 app.id 可以配置 yyspring.apollo.app.id=xxx, 将会使用该值作为 apollo 的 app.id 值

# 定义配置拉取服务地址
默认情况下，配置在 apollo-env.properties 中各个环境如下：
```properties
  "DEV" : "http://config.apollo.yygamedev.com",
  "LPT" : "http://config.local",
  "PROD" : "https://config-apollo.yy.com",
  "LOCAL" : "http://localhost:8080",
  "PRO" : "http://config.local",
  "UAT" : "http://config.local",
  "TEST" : "http://config.apollo.yygamedev.com",
  "FAT" : "http://config.local"
```

如果你想使用自己的配置拉取地址，可以在不同环境的配置文件 application-${env}.properties 中配置：
```properties
yyspring.apollo.meta=http://config.apollo.yygamedev.com
```
