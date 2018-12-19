package com.duowan.common.ipowner.seeker;

import com.duowan.common.ipowner.util.Constants;
import com.duowan.common.timer.PerDayPeriod;
import com.duowan.common.timer.Period;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Arvin
 */
public class IpSeekerDistrictProvider extends AbstractIpSeekerProvider {

    private String ipdatxDownloadUrl;

    private String ipdatxFilePath;

    /**
     * 是否异步进行初始化
     */
    private boolean asyncInit;

    public IpSeekerDistrictProvider() {
    }

    public IpSeekerDistrictProvider(String ipdatxDownloadUrl, String ipdatxFilePath, boolean asyncInit) {
        this.ipdatxDownloadUrl = ipdatxDownloadUrl;
        this.ipdatxFilePath = ipdatxFilePath;
        this.asyncInit = asyncInit;
    }

    public void setIpdatxDownloadUrl(String ipdatxDownloadUrl) {
        this.ipdatxDownloadUrl = ipdatxDownloadUrl;
    }

    public void setIpdatxFilePath(String ipdatxFilePath) {
        this.ipdatxFilePath = ipdatxFilePath;
    }

    public void setAsyncInit(boolean asyncInit) {
        this.asyncInit = asyncInit;
    }

    @Override
    public Period getPeriod() {
        Period period = super.getPeriod();
        if (null == period) {
            period = new PerDayPeriod(5, 30);
            setPeriod(period);
        }
        return period;
    }

    @Override
    protected String getIpdatxFilePath() {
        return ipdatxFilePath;
    }

    @Override
    protected String getIpdatxDownloadUrl() {
        return ipdatxDownloadUrl;
    }

    @Override
    protected boolean isAsyncInit() {
        return asyncInit;
    }

    @Override
    protected String getIpSeekerClassName() {
        return IpDistrictSeeker.class.getSimpleName();
    }

    @Override
    protected IpSeeker createIpSeeker() {
        return new IpDistrictSeeker(getIpdatxFilePath());
    }

    @Override
    protected void customPreInit() {
        if (StringUtils.isBlank(ipdatxFilePath)) {
            ipdatxFilePath = Constants.DEFAULT_IP_DISTRICT_FILE_PATH;
        }
        if (StringUtils.isBlank(ipdatxDownloadUrl)) {
            ipdatxDownloadUrl = Constants.DEFAULT_IP_DISTRICT_DOWNLOAD_URL;
        }
    }

}
