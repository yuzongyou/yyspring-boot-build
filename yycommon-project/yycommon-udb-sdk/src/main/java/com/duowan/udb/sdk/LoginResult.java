package com.duowan.udb.sdk;

import java.util.List;

/**
 * 登录互联结果
 *
 * @author Arvin
 * @time 2018/1/15 18:15
 */
public class LoginResult {

    /** 跟踪 ID， 可以是 Token， 能唯一标识一次登录就行 */
    private String id;

    /** 过期时间，默认是5分钟 */
    private int expiresIn = 300;

    /** 成功写入后的跳转地址 */
    private String jumpUrl;

    /** 写入失败后的跳转地址 */
    private String failUrl;

    /** 登录类型 */
    private String loginType;

    /** Cookie 列表 */
    private List<CK> ckList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getFailUrl() {
        return failUrl;
    }

    public void setFailUrl(String failUrl) {
        this.failUrl = failUrl;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public List<CK> getCkList() {
        return ckList;
    }

    public void setCkList(List<CK> ckList) {
        this.ckList = ckList;
    }
}
