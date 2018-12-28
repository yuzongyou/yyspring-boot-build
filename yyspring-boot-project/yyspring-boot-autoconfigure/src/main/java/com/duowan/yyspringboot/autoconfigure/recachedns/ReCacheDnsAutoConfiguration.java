package com.duowan.yyspringboot.autoconfigure.recachedns;

import com.duowan.common.dns.DnsInterceptor;
import com.duowan.common.recachedns.DnsCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/7 10:50
 */
@Configuration
@ConditionalOnClass({com.duowan.common.recachedns.DnsCacheUtil.class})
@EnableConfigurationProperties(ReCacheDnsProperties.class)
public class ReCacheDnsAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ReCacheDnsAutoConfiguration.class);

    public ReCacheDnsAutoConfiguration(ReCacheDnsProperties properties, @Autowired(required = false) DnsInterceptor dnsInterceptor) throws Exception {
        initReCacheDns(properties, dnsInterceptor);
    }

    private void initReCacheDns(ReCacheDnsProperties properties, DnsInterceptor dnsInterceptor) throws Exception {

        StringBuilder infoBuilder = new StringBuilder();

        DnsCacheUtil.setMaxDnsMillis(properties.getMaxDnsMillis());
        DnsCacheUtil.setDnsCacheTime(properties.getCacheSeconds());

        infoBuilder.append("DNS缓存时间： ").append(properties.getCacheSeconds()).append(" 秒， 预估DNS解析最大耗时： ")
                .append(properties.getMaxDnsMillis()).append(" 毫秒");

        if (dnsInterceptor != null) {
            DnsCacheUtil.setDnsInterceptor(dnsInterceptor);
            infoBuilder.append("， DNS 拦截器： ").append(dnsInterceptor.getClass());
        }

        // 启用自动缓存刷新
        DnsCacheUtil.enabledAutoReCache();

        logger.info("开启DNS自动刷新缓存成功： {}", infoBuilder.toString());
    }


}
