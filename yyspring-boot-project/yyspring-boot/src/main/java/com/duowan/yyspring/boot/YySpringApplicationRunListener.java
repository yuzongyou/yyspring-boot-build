package com.duowan.yyspring.boot;

import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.ReflectUtil;
import com.duowan.common.utils.exception.AssertFailException;
import com.duowan.yyspring.boot.annotations.YYSpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.List;
import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/1 17:36
 */
public class YySpringApplicationRunListener implements SpringApplicationRunListener, Ordered {

    private final SpringApplication application;

    private final String[] args;

    public YySpringApplicationRunListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    private void initialize() {
        Set<Object> sources = application.getAllSources();
        Class<?> sourceClass = validateSourcesThenReturnFirstHasYYSpringApplicationAnnotationSource(sources);
        // 初始化
        AppContext.initialize(sourceClass);

        YYSpringBootApplication applicationAnn = sourceClass.getAnnotation(YYSpringBootApplication.class);
        boolean tryEnabledDefaultBeanNameGenerator = true;
        if (null != applicationAnn) {
            tryEnabledDefaultBeanNameGenerator = applicationAnn.tryEnabledDefaultBeanNameGenerator();
        }
        if (tryEnabledDefaultBeanNameGenerator) {
            // 如果用户没有自定义 BeanNameGenerator 则默认给一个
            Object beanNameGenerator = ReflectUtil.getFieldValue(application, "beanNameGenerator");
            if (null == beanNameGenerator) {
                application.setBeanNameGenerator(new DefaultAnnotationBeanNameGenerator());
            }
        }
    }

    private static Class<?> validateSourcesThenReturnFirstHasYYSpringApplicationAnnotationSource(Set<Object> sources) {
        String message = "必须提供应用Source对象";
        if (null == sources || sources.isEmpty()) {
            throw new AssertFailException(message);
        }

        Class<?> firstNullSource = null;
        Class<?> firstHasYySpringApplicationAnnotationSource = null;
        for (Object sourceObj : sources) {
            if (null != sourceObj) {
                Class<?> sourceClass = getObjectClass(sourceObj);
                if (firstNullSource == null) {
                    firstNullSource = sourceClass;
                }
                YYSpringBootApplication ann = sourceClass.getAnnotation(YYSpringBootApplication.class);
                if (ann != null) {
                    AssertUtil.assertNull(firstHasYySpringApplicationAnnotationSource,
                            "@YYSpringBootApplication 注解只能在一个启动来源上设置！");
                    firstHasYySpringApplicationAnnotationSource = sourceClass;
                }
            }
        }
        if (null != firstHasYySpringApplicationAnnotationSource) {
            return firstHasYySpringApplicationAnnotationSource;
        }
        if (null != firstNullSource) {
            return firstNullSource;
        }
        throw new AssertFailException(message);
    }

    private static Class<?> getObjectClass(Object object) {
        if (null == object) {
            return null;
        }
        if (object instanceof Class) {
            return (Class<?>) object;
        } else {
            return object.getClass();
        }
    }

    @Override
    public void starting() {
        initialize();
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        environment.getPropertySources().addLast(new MapPropertySource("projectInfo", AppContext.getProjectInfoMap()));
        environment.getPropertySources().addLast(new MapPropertySource("yyApplicationProperties", AppContext.getApplicationProperties()));
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        AppContext.setAcx(context);
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        Logger appContextLogger = LoggerFactory.getLogger(AppContext.class);
        List<String> infoList = AppContext.getInitInfo();
        for (String info : infoList) {
            appContextLogger.info(info);
        }
    }

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }

    @Override
    public int getOrder() {
        return 1;
    }
}
