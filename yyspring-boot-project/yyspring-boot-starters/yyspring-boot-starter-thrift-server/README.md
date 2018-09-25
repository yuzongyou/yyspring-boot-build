# Thrift 服务端自动配置
<font color=#0099ff size="8" face="黑体">**阅读本文档前，可以前往以下地址查看非 SpringBoot 环境下使用 Thrift 服务端的说明：**[yycommon-thrift-server](../../../yycommon-project/yycommon-thrift-server)</font>


本模块主要是集成到 <code>SpringBoot</code> 中使用，实现自动配置<code>Bean</code>和<code>@ThriftService</code>支持:
1. 开发者通过在 springboot 配置文件中配置 yyspring.thrift.server.xxx 相关属性
2. 业务代码中使用 <code>@ThriftService</code> 指定要进行发布的 Thrift 接口

PS： 默认情况下，只需要在实现了 Thrift IDL 生成的接口，然后用 <code>@ThriftService</code> 标识这个Bean即可成功发布Thrift






