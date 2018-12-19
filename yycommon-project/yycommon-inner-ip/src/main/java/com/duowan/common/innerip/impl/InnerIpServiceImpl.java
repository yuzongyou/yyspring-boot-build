package com.duowan.common.innerip.impl;

import com.duowan.common.innerip.InnerIpService;
import com.duowan.common.innerip.OfficialIp;
import com.duowan.common.innerip.OfficialIpParser;
import com.duowan.common.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author xiajiqiu
 * @version 1.0
 * @since 2018/9/8 22:38
 */
public class InnerIpServiceImpl implements InnerIpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InnerIpServiceImpl.class);

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * 默认办公网IP获取地址
     */
    private static final String DEFAULT_OFFICIAL_URL = "http://webapi.sysop.duowan.com:62175/office/ip_desc.txt";

    /**
     * 办公网查询地址
     */
    private String officialUrl = DEFAULT_OFFICIAL_URL;

    private Map<String, OfficialIp> officialIpMap = new HashMap<>();

    /**
     * 是否进行了调度
     */
    private volatile boolean hadScheduled = false;

    /**
     * 单位是秒
     */
    private int refreshInterval = 24 * 60 * 60;

    /**
     * IP 解析器
     */
    private OfficialIpParser officialIpParser;

    public String getOfficialUrl() {
        return officialUrl;
    }

    public void setOfficialUrl(String officialUrl) {
        this.officialUrl = officialUrl;
    }

    public OfficialIpParser getOfficialIpParser() {
        if (officialIpParser == null) {
            officialIpParser = new OfficialIpParser();
            return officialIpParser;
        }
        return officialIpParser;
    }

    public void setOfficialIpParser(OfficialIpParser officialIpParser) {
        this.officialIpParser = officialIpParser;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    /**
     * 设置刷新时间间隔，单位是秒
     *
     * @param refreshInterval
     */

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    /**
     * 初始化
     */
    public void init() {
        try {
            initBySync();
        } finally {
            // 没有调度执行，调度任务刷新缓存
            if (!hadScheduled) {
                scheduledExecutorService.scheduleAtFixedRate(() -> {
                    try {
                        initBySync();
                    } catch (Exception e) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug(e.getMessage(), e);
                        }
                    }
                }, 0L, getRefreshInterval() * 1000L, TimeUnit.MILLISECONDS);

                hadScheduled = true;
            }
        }

    }

    /**
     * 同步初始化
     *
     */
    private void initBySync() {

        long begTime = System.currentTimeMillis();
        LOGGER.info("正在刷新办公网IP");

        String responseText = HttpUtil.doGet(officialUrl);

        String[] lines = responseText.split("[\r\n]+");

        Map<String, OfficialIp> tempOfficialIpMap = new HashMap<>(lines.length);

        OfficialIpParser parser = getOfficialIpParser();

        for (String line : lines) {
            OfficialIp officialIp = parser.parse(line);
            if (null != officialIp) {
                tempOfficialIpMap.put(StringUtils.trim(officialIp.getIp()), officialIp);
            }
        }

        this.officialIpMap = tempOfficialIpMap;

        LOGGER.info("完成一次办公网IP刷新，耗时 {}", (System.currentTimeMillis() - begTime));

    }

    @Override
    public boolean isOfficialIp(String ip) {
        return this.officialIpMap.containsKey(StringUtils.trim(ip));
    }

    @Override
    public OfficialIp getOfficialIp(String ip) {
        return this.officialIpMap.get(StringUtils.trim(ip));
    }
}
