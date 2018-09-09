package com.duowan.common.ipowner.seeker;

import com.duowan.common.ipowner.util.DownloadUtil;
import com.duowan.common.timer.AbstractTimer;
import com.duowan.common.timer.Period;
import com.duowan.common.timer.TimerUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 抽象类
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/7/27 16:44
 */
public abstract class AbstractIpSeekerProvider extends AbstractTimer implements IpSeekerProvider {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected int instanceCount = Runtime.getRuntime().availableProcessors() * 2;

    private volatile int nextIpSeekerIndex = 0;

    private IpSeeker[] ipSeekers;

    /**
     * 上一次更新历史信息存储路径
     */
    private String lastIpdatUpdateInfoFilePath;

    /**
     * 定时任务
     */
    private Period period;

    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override
    public IpSeeker getSeeker() {
        if (this.ipSeekers == null || this.ipSeekers.length < 1) {
            return null;
        }
        int index = getNextIpSeekerIndex();
        return ipSeekers[index];
    }

    private int getNextIpSeekerIndex() {
        int index = nextIpSeekerIndex++;
        if (index >= ipSeekers.length) {
            index = 0;
            nextIpSeekerIndex = 1;
        }
        return index;
    }

    /**
     * 获取Ip文件存储路径
     */
    protected abstract String getIpdatxFilePath();

    /**
     * 获取Ip文件下载路径
     */
    protected abstract String getIpdatxDownloadUrl();

    /**
     * 是否异步初始化
     */
    protected abstract boolean isAsyncInit();

    @Override
    public Period getPeriod() {
        return this.period;
    }

    public void init() {
        customPreInit();
        checkEnv();
        load(isAsyncInit(), false);
        // 启动定时器
        TimerUtil.scheduleTimerExecute(this);
    }

    protected void customPreInit() {

    }

    private void checkEnv() {
        if (StringUtils.isBlank(getIpdatxDownloadUrl()) || StringUtils.isBlank(getIpdatxFilePath())) {
            throw new RuntimeException("未提供Ipdatx文件下载和存储地址");
        }

        File file = new File(getIpdatxFilePath());
        File folder = file.getParentFile();
        // 文件夹不存在的话就创建
        if (!folder.exists()) {
            logger.info("Create Folder: [" + folder.getAbsolutePath() + "]");
            if (!folder.mkdirs()) {
                throw new RuntimeException("创建ip.dat存储文件夹失败：[" + folder.getAbsolutePath() + "]");
            }
        }

        this.lastIpdatUpdateInfoFilePath = folder.getAbsolutePath() + File.separator + "lastIpdatUpadteInfo_" + getAdapterProviderId();

        logger.info("IpDatx 文件下载地址： [" + getIpdatxDownloadUrl() + "]");
        logger.info("IpDatx 文件本地地址： [" + getIpdatxFilePath() + "]");
    }

    protected String getProviderId() {
        return null;
    }

    private String getAdapterProviderId() {
        String providerId = getProviderId();
        if (StringUtils.isBlank(providerId)) {
            return this.getClass().getSimpleName();
        }
        return providerId;
    }

    @Override
    public void start() {
        initSync(true);
    }

    /**
     * 初始化
     */
    private void load(boolean isAsyncInit, final boolean forceDownload) {

        final String className = this.getClass().getSimpleName();
        if (isAsyncInit) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        initSync(forceDownload);
                    } catch (Exception e) {
                        logger.error("初始化本地提供者[" + className + "]失败: " + e.getMessage(), e);
                    }
                }
            }).start();
        } else {
            initSync(forceDownload);
        }

    }

    private void initSync(boolean forceDownload) {
        downloadIpdatFile(forceDownload);
        initIpSeekers();
    }

    /**
     * 初始化 IpSeeker
     */
    private void initIpSeekers() {
        String className = getIpSeekerClassName();
        logger.info("开始初始化" + className + "，预计实例化[" + instanceCount + "] 个 " + className + " 对象！");
        long begTime = System.currentTimeMillis();
        ipSeekers = new IpSeeker[instanceCount];
        for (int i = 0; i < instanceCount; ++i) {
            logger.info(">> 正在创建第[" + (i + 1) + "] 的 " + className + " 实例.....");
            long st = System.currentTimeMillis();
            ipSeekers[i] = createIpSeeker();
            long et = System.currentTimeMillis();
            logger.info(">> 第[" + (i + 1) + "] 的 " + className + " 实例创建完成，耗时[" + (et - st) + "] 毫秒！");

        }
        long endTime = System.currentTimeMillis();
        logger.info(this.getClass().getSimpleName() + " 初始化完成，共实例化了[" + instanceCount + "] 个 " + className + " 对象， 耗时[" + (endTime - begTime) + "] 毫秒！");
    }

    protected abstract String getIpSeekerClassName();

    protected abstract IpSeeker createIpSeeker();


    /**
     * 下载最新的ip.dat文件
     *
     * @param forceDownload 是否强制进行下载
     */
    private void downloadIpdatFile(boolean forceDownload) {
        if (forceDownload || isRequiredStartDownload()) {
            String ipdatDownloadUrl = getIpdatxDownloadUrl();
            logger.info("正在下载最新的[" + getIpdatxDownloadUrl() + "]文件：" + ipdatDownloadUrl);
            try {
                long begTime = System.currentTimeMillis();
                DownloadUtil.download(ipdatDownloadUrl, getIpdatxFilePath());
                long endTime = System.currentTimeMillis();
                logger.info("[" + ipdatDownloadUrl + "] 文件下载完成，共计耗时[" + (endTime - begTime) + "] 毫秒！");
                logLastUpdateInfo();
            } catch (Exception e) {
                logger.error("[" + ipdatDownloadUrl + "] 文件下载失败，error=" + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        } else {
            logger.info("当日不需要重新下载[" + getIpdatxFilePath() + "]文件！");
        }
    }

    private boolean isRequiredStartDownload() {
        LastUpdateInfo lastUpdateInfo = getLastUpdateInfo();
        if (null == lastUpdateInfo) {
            return true;
        }

        // 当日的不需要重新下载
        if (isCurrentDay(lastUpdateInfo.getLastUpdateTime())) {
            return false;
        }
        return true;
    }

    /**
     * 给定的日期是否是当前
     *
     * @param date
     * @return
     */
    private boolean isCurrentDay(Date date) {

        if (null == date) {
            return false;
        }

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);

        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTimeInMillis(System.currentTimeMillis());

        if (calendarDate.get(Calendar.YEAR) == calendarToday.get(Calendar.YEAR)
                && calendarDate.get(Calendar.MONTH) == calendarToday.get(Calendar.MONTH)
                && calendarDate.get(Calendar.DAY_OF_MONTH) == calendarToday.get(Calendar.DAY_OF_MONTH)
                ) {
            return true;
        }
        return false;
    }

    private void logLastUpdateInfo() {
        LastUpdateInfo lastUpdateInfo = new LastUpdateInfo();
        lastUpdateInfo.setLastUpdateTime(new Date());

        BufferedWriter writer = null;
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            writer = new BufferedWriter(new FileWriter(new File(this.lastIpdatUpdateInfoFilePath)));

            writer.write(dateFormat.format(lastUpdateInfo.getLastUpdateTime()));

            writer.flush();
        } catch (Exception e) {
            logger.warn("记录最后一次更新[ip.dat]信息失败： " + e.getMessage(), e);
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private LastUpdateInfo getLastUpdateInfo() {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(new File(this.lastIpdatUpdateInfoFilePath)));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String line;
            long lineNum = 0;
            LastUpdateInfo lastUpdateInfo = new LastUpdateInfo();
            while ((line = reader.readLine()) != null) {
                lineNum++;

                // 第一行就是更新时间
                if (lineNum == 1) {
                    lastUpdateInfo.setLastUpdateTime(dateFormat.parse(line.trim()));
                }
            }

            return lastUpdateInfo;

        } catch (Exception e) {
            return null;
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    class LastUpdateInfo {
        /**
         * 最后一次更新时间
         */
        private Date lastUpdateTime;

        public Date getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(Date lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }
    }
}
