package com.duowan.yyspringboot.autoconfigure.virtualdns;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/1 19:52
 */
@Configuration
@ConditionalOnClass({com.duowan.common.virtualdns.VirtualDnsUtil.class, com.duowan.common.dns.util.InetAddressUtil.class})
@EnableConfigurationProperties(VirtualDnsProperties.class)
public class VirtualDnsAutoConfiguration {

}
