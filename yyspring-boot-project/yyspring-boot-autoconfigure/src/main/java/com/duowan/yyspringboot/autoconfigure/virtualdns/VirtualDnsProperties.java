package com.duowan.yyspringboot.autoconfigure.virtualdns;

import com.duowan.yyspring.boot.AppContext;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 虚拟DNS属性配置
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/1 19:50
 */
@ConfigurationProperties(prefix = VirtualDnsProperties.PROPERTIES_PREFIX)
public class VirtualDnsProperties {

    public static final String PROPERTIES_PREFIX = "yyspring.virtualdns";
    /**
     * 匹配的环境，如果是空字符串则表示所有环境生效，默认是开发环境才生效
     */
    private String[] matchEnvs = new String[]{AppContext.ENV_DEV};

    /**
     * HOSTS 文件搜索路径，默认是和 application.properties 的路径一致
     **/
    private String[] hostsPaths = null;

    /** 是否忽略不存在的文件, 默认是忽略的 **/
    private boolean ignoredUnknownFile = true;

    /** 重新加载的时间间隔，单位是毫秒，默认是不进行重新加载， -1 表示不需要重新加载 **/
    private long reloadIntervalMillis = -1;

    public String[] getMatchEnvs() {
        return matchEnvs;
    }

    public void setMatchEnvs(String[] matchEnvs) {
        this.matchEnvs = matchEnvs;
    }

    public String[] getHostsPaths() {
        return hostsPaths;
    }

    public void setHostsPaths(String[] hostsPaths) {
        this.hostsPaths = hostsPaths;
    }

    public boolean isIgnoredUnknownFile() {
        return ignoredUnknownFile;
    }

    public void setIgnoredUnknownFile(boolean ignoredUnknownFile) {
        this.ignoredUnknownFile = ignoredUnknownFile;
    }

    public long getReloadIntervalMillis() {
        return reloadIntervalMillis;
    }

    public void setReloadIntervalMillis(long reloadIntervalMillis) {
        this.reloadIntervalMillis = reloadIntervalMillis;
    }
}
