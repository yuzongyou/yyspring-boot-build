# 系统告警
本模块主要是集成了升龙的警报系统，方便开发者接入升龙警报系统，正常情况下，升龙上新建一个项目，会有相应的警报系统配置
入口为： 升龙控制台 --> 顶栏中的服务 --> 警报服务 即可进入该项目的警报配置页面：
![alt text](docs/img/alarm_entrance.png)

![alt text](docs/img/alarm_admin.png)

该警报服务允许你自定义警报编号等相关操作，一般情况下，默认的警报配置已经足够了。

# 如何使用本模块
使用本模块非常简单，只需要引入下面这个依赖即可：
```xml
<dependency>
    <groupId>com.duowan</groupId>
    <artifactId>esb-alarm-starter</artifactId>
    <version>0.1.1-SNAPSHOT</version>
</dependency>
```

当然，有一些配置项是需要配置的，也会有默认值：

| **配置项** | **默认值** | **说明** |
| :---- | :---- | :---- |
| extend.alarm.enabled | prod环境默认是true，其他环境是false | 是否需要真的发送告警信息 |
| extend.alarm.dsnUrl |  | 如果需要自定义警报编号的功能，就需要配置这个，可参考<br>[**自定义警报编号**](#自定义警报编号) |

然后你就可以在你的代码中使用警报了：

```java
Alarm.alarm("WarnTest");
```

# 自定义警报编号
如果你的项目真的需要自定义警报编号的话，那么也是支持的，首先要在 pom 中引入下面的依赖：

```xml
<dependency>
    <groupId>com.duowan.bs.zxa</groupId>
    <artifactId>zxa</artifactId>
    <version>1.1.0-SNAPSHOT</version>
</dependency>
```

然后设置 dsnUrl:
```properties
extend.alarm.dsnUrl=http://leopardtest:1b680dda9a1eab4f5a50@alarm.defensor.game.yy.com:80?DWENV=prod
```

**extend.alarm.dsnUrl** 配置项的值获取方式如下：
> 1. 首先进入警报系统
> 2. 点击 客户端接入 --> Java:
![alt text](docs/img/alarm_dsnUrl.png)

> 最后是使用，如上如所示，新建了一个 alarmNo = test_alarm, 那么代码中可以这样告警：
```java
Alarm.alarm("test_alarm", "WarnTest");
```

# log4j error 日志直接发送告警集成
如果你系统在 error 日志打印的时候同时发送告警信息，本模块也提供了这种支持，需要在你的 log4j2 的配置文件中使用下面的 Appender

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="INFO">
    <Properties>
        <Property name="PID">????</Property>
        <Property name="LOG_EXCEPTION_CONVERSION_WORD">%xwEx</Property>
        <Property name="LOG_LEVEL_PATTERN">%5p</Property>
    </Properties>

    <Appenders>
        <!-- Console Appender -->
        <Console name="Console" target="SYSTEM_OUT" follow="true">
            <PatternLayout pattern="%d %p [%X,%t] - [%clr{%-40.40c{1.}}] - &lt;%m&gt;%n"/>
        </Console>

        <!-- AlarmRollingFile Appender -->
        <AlarmRollingFile name="AlarmRollingFile" fileName="/data2/log/resin/web-all.log"
                     filePattern="/data2/log/resin/web-all-%d{yyyy-MM-dd}.log.gz">

            <PatternLayout pattern="%d %p [%X,%t] - [%clr{%-40.40c{1.}}] - &lt;%m&gt;%n"/>
            <!--<PatternLayout pattern="${sys:FILE_LOG_PATTERN}"/>-->
            <Policies>
                <!-- 分割策略，可參考： http://logging.apache.org/log4j/2.x/manual/appenders.html#RollingFileAppender -->

                <!-- 启动的时候就会创建一个日志文件并且会打包 -->
                <!--<OnStartupTriggeringPolicy/>-->

                <!-- 每天产生一个日志文件, interval = 1 -->
                <TimeBasedTriggeringPolicy interval="1" modulate="true"/>
                <!-- 日志达到1K自动生成 -->
                <!--<SizeBasedTriggeringPolicy size="1K"/>-->
            </Policies>
        </AlarmRollingFile>
    </Appenders>
    <Loggers>
        <Root level="INFO">
            <AppenderRef ref="AlarmRollingFile"/>
        </Root>
    </Loggers>
</Configuration>
```

然后业务代码中，如果使用了 logger.error("出错啦！"); 就会发送告警信息

# logback error 日志直接发送告警集成
如果你系统在 error 日志打印的时候同时发送告警信息，本模块也提供了这种支持，需要在你的 logback 的配置文件中使用下面的 Appender
```xml
<?xml version="1.0" encoding="UTF-8"?>

<configuration debug="false">
    <conversionRule conversionWord="clr" converterClass="org.springframework.boot.logging.logback.ColorConverter"/>
    <conversionRule conversionWord="wex"
                    converterClass="org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter"/>
    <conversionRule conversionWord="wEx"
                    converterClass="org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter"/>
    <property name="CONSOLE_LOG_PATTERN"
              value="${CONSOLE_LOG_PATTERN:-%clr(%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>
    <property name="FILE_LOG_PATTERN"
              value="${FILE_LOG_PATTERN:-%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}} ${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] %-40.40logger{39} : %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>

    <logger name="org.apache.catalina.startup.DigesterFactory" level="ERROR"/>
    <logger name="org.apache.catalina.util.LifecycleBase" level="ERROR"/>
    <logger name="org.apache.coyote.http11.Http11NioProtocol" level="WARN"/>
    <logger name="org.apache.sshd.common.util.SecurityUtils" level="WARN"/>
    <logger name="org.apache.tomcat.util.net.NioSelectorPool" level="WARN"/>
    <logger name="org.eclipse.jetty.util.component.AbstractLifeCycle" level="ERROR"/>
    <logger name="org.hibernate.validator.internal.util.Version" level="WARN"/>

    <property name="LOG_ERROR_FILE" value="error.log"/>

    <!--<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">-->
    <appender name="STDOUT" class="com.duowan.logback.logback.ConsoleAppender">
        <encoder>
            <pattern>${CONSOLE_LOG_PATTERN}</pattern>
        </encoder>
    </appender>

    <appender name="FILE_ERROR" class="com.duowan.common.alarm.logback.AlarmRollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <encoder>
            <pattern>${FILE_LOG_PATTERN}</pattern>
        </encoder>
        <file>${LOG_ERROR_FILE}</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${LOG_ERROR_FILE}.%d{yyyy-MM-dd}.%i.gz</fileNamePattern>
            <maxFileSize>${LOG_FILE_MAX_SIZE:-10MB}</maxFileSize>
            <maxHistory>${LOG_FILE_MAX_HISTORY:-0}</maxHistory>
        </rollingPolicy>
    </appender>

    <!-- 日志输出级别 -->
    <root level="INFO">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="FILE_ERROR"/>
    </root>
</configuration>
```



