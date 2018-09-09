package com.duowan.yyspringboot.autoconfigure.innerip;

import com.duowan.common.innerip.impl.InnerIpServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiajiqiu
 * @version 1.0
 * @since 2018/9/9 15:58
 */
@Configuration
@ConditionalOnClass({com.duowan.common.innerip.impl.InnerIpServiceImpl.class, com.duowan.common.innerip.InnerIpService.class})
public class InnerIpAutoConfiguration {

    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    public InnerIpServiceImpl innerIpServiceImpl() {
        return new InnerIpServiceImpl();
    }

}
