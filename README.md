# YY SpringBoot
结合YY实际情况的springboot启动器

# 如何deploy
如果要deploy，并且deploy同样的版本，先在最顶层的 pom.xml 中修改，即与本文件同级目录下的 pom.xml, 修改<version>2.0.0-SNAPSHOT</version>和 <yyproject.version>2.0.0-SNAPSHOT</yyproject.version>为你要发布的版本，然后执行如下命令:
```cmd
mvn -N versions:update-child-modules
```
以上命令会把其他项目目录都改成统一的版本，然后你在进行 deploy：
```cmd
mvn clean deploy
```