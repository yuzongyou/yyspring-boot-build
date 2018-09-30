package com.duowan.wxmpsdk.model;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/29 16:31
 */
public class Code2Session {

    /**
     * 用户唯一标识
     **/
    private String openid;

    /**
     * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明: https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html。
     **/
    private String unionid;

    /**
     * 会话密钥
     **/
    private String sessionKey;

    public Code2Session() {
    }

    public Code2Session(String openid, String unionid, String sessionKey) {
        this.openid = openid;
        this.unionid = unionid;
        this.sessionKey = sessionKey;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
