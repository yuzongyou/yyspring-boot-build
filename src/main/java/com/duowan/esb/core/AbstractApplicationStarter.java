package com.duowan.esb.core;

import com.duowan.common.utils.ConvertUtil;
import com.duowan.common.utils.ReflectUtil;
import com.duowan.yyspring.boot.AppContext;
import com.duowan.yyspring.boot.SpringApplicationBuilderConfigurer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.io.IOException;

/**
 * 非web程序启动器，命令行程序启动器
 *
 * @author Arvin
 */
public abstract class AbstractApplicationStarter implements CommandLineRunner {

    static {
        AppContext.prepareAppEnv();
    }

    /**
     * 应用启动
     *
     * @param mainClass 主 Class
     * @param args      命令行参数
     */
    public static void startApp(Class<?> mainClass, String[] args) throws IOException {
        startApp(mainClass, args, null);
    }

    /**
     * 应用启动
     *
     * @param mainClass  主 Class
     * @param args       命令行参数
     * @param configurer 配置类
     */
    protected static void startApp(Class<?> mainClass, String[] args, SpringApplicationBuilderConfigurer configurer) throws IOException {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(mainClass);

        if (null != configurer) {
            configurer.configure(builder);
        }

        // 非web环境执行
        builder.web(false);

        SpringApplication application = builder.build();

        // 如果用户没有自定义 BeanNameGenerator 则默认给一个
        Object beanNameGenerator = ReflectUtil.getFieldValue(application, "beanNameGenerator");
        if (null == beanNameGenerator) {
            application.setBeanNameGenerator(new EsbAnnotationBeanNameGenerator());
        }

        AppContext.setAcx(application.run(args));
    }

    @Override
    public final void run(String... args) throws Exception {

        runTask(AppContext.getAcx(), new DefaultApplicationArguments(args), AppContext.getEnvironment());

        if (!exitAfterRunTask()) {
            Thread.currentThread().join();
        }

    }

    /**
     * 运行完成runTask后是否退出程序
     */
    protected static final String EXIT_AFTER_RUN_TASK_KEY = "app.exit.after.runTask";

    /**
     * 是否在运行完成 runTask 任务方法后退出系统
     *
     * @return true - 退出， false - 不退出（通常是定时任务）
     */
    protected boolean exitAfterRunTask() {

        return ConvertUtil.toBoolean(AppContext.getAppProperty(EXIT_AFTER_RUN_TASK_KEY, "true"), true);

    }

    /**
     * 运行任务
     *
     * @param applicationContext Spring 上下文
     * @param cmdArguments       命令行参数
     * @param environment        当前环境
     */
    public abstract void runTask(ApplicationContext applicationContext, ApplicationArguments cmdArguments, Environment environment);
}
