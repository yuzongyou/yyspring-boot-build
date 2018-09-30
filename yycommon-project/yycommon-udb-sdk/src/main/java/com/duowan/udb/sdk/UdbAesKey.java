package com.duowan.udb.sdk;

/**
 * @author Arvin
 * @time 2018/4/2 14:35
 */
public class UdbAesKey {

    /** UDB 的 APPID */
    private String appId;

    /** 新的KEY */
    private String newkey;

    /** 老的KEY */
    private String oldkey;

    /** 过期时间 */
    private long expireTime;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNewkey() {
        return newkey;
    }

    public void setNewkey(String newkey) {
        this.newkey = newkey;
    }

    public String getOldkey() {
        return oldkey;
    }

    public void setOldkey(String oldkey) {
        this.oldkey = oldkey;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isExpired() {
        // 相差三秒差距就要更新
        return System.currentTimeMillis() >= expireTime - 3000;
    }
}
