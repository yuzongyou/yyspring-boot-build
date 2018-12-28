package com.duowan.yyspringboot.autoconfigure.virtualdns;

import com.duowan.yyspring.boot.SpringApplicationRunListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/29 10:33
 */
public class VirtualDnsApplicationRunListener extends SpringApplicationRunListenerAdapter {

    private boolean needAutoConfigurer;

    public VirtualDnsApplicationRunListener(SpringApplication application, String[] args) {
        super(application, args);
        this.needAutoConfigurer = isClassesImported(
                "com.duowan.common.vdns.VirtualDnsUtil",
                "com.duowan.common.dns.DnsResolver"
        );
    }

    @Override
    protected boolean needAutoConfigurer() {
        return needAutoConfigurer;
    }

    @Override
    public void doEnvironmentPrepared(ConfigurableEnvironment environment) {
        VirtualDnsProperties properties = bindProperties(VirtualDnsProperties.PROPERTIES_PREFIX, VirtualDnsProperties.class);

        VdnsHelper.initVirtualDns(properties == null ? new VirtualDnsProperties() : properties);
    }

}
