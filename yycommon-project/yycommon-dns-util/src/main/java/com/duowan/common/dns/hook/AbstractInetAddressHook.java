package com.duowan.common.dns.hook;

import com.duowan.common.dns.DnsResolver;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/7 9:21
 */
public abstract class AbstractInetAddressHook implements InetAddressHook {

    protected DnsResolver customDnsResolver;

    protected boolean hookEnabled;

    public AbstractInetAddressHook(DnsResolver customDnsResolver) {
        this.customDnsResolver = customDnsResolver;
    }

    public DnsResolver getCustomDnsResolver() {
        return customDnsResolver;
    }

    public void setCustomDnsResolver(DnsResolver customDnsResolver) {
        this.customDnsResolver = customDnsResolver;
    }

    public boolean isHookEnabled() {
        return hookEnabled;
    }

    public void setHookEnabled(boolean hookEnabled) {
        this.hookEnabled = hookEnabled;
    }
}
