# UDB 支持
支持 UDB的登录验证，包含本地，远程强验证，UDB 退出
    
## UDB 验证说明
UDB 提供的 jar 支持两种验证方式，本地和远程验证，下面说说两种验证方式的区别：
### 本地验证：
    本地验证是只会验证 Cookie 中的字段是否符合一定的AES和RSA签名规则，两种验证方式：
        1. username, yyuid 之一 + oauthCookie 验证
        2. username, yyuid 之一 + udb_oar 验证

    UserinfoForOauth 支持两种方式，一种是通过直接读取 HttpServletRequest 中的 cookie验证
    另一种是直接通过 username, yyuid 之一 + (oauthCookie, udb_oar 之一) + appId + appKey 验证

### 远程验证：
    远程验证可以称之为强验证，同一时刻只会有一个地方的验证通过，举个例子：
        1. 用户在 Firefox 浏览器登录了帐号 A
        2. 然后用户又在 Chrome 浏览器登录了帐号 A

    如果采用强验证，那么 Firefox 中再次验证的时候，本地验证会通过，但是强验证会失败
    只有 Chrome 中强验证成功

本组件提供以下能力，简化UDB验证服务的使用
1. 通过 udbAppId, udbAppKey, HttpServletRequest 进行验证
2. 通过 udbAppId, udbAppKey, + (username, yyuid) + oauthCookie 和 udb_oar 进行验证