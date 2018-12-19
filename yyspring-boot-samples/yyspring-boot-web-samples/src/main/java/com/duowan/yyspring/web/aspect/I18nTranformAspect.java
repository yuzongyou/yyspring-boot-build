package com.duowan.yyspring.web.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/14 11:28
 */
@Component
@Aspect
public class I18nTranformAspect {

    @Pointcut("execution(public * *..*Controller.*(..))")
    public void i18nTranslate() {
    }

    /**
     * 后置返回通知
     * 这里需要注意的是:
     * 如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息
     * 如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
     * returning 限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
     */
    @AfterReturning(pointcut = "i18nTranslate()", returning = "response")
    public void doAfterReturningAdvice1(JoinPoint joinPoint, Object response) {

        System.out.println("获取输入结果： " + response);

    }

    @Around("execution(public * *..*Controller.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("拦截了请求： " + joinPoint);

        return joinPoint.proceed();
    }

}
