package com.duowan.common.innerip;

import java.util.Date;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/8 22:22
 */
public class OfficialIp {

    /**
     * IP 地址
     */
    private final String ip;

    /**
     * 办公网地址IP说明
     */
    private final String desc;

    /**
     * 添加时间
     */
    private final Date addTime;

    public OfficialIp(String ip, String desc, Date addDate) {
        this.ip = ip;
        this.desc = desc;
        this.addTime = addDate;
    }

    public String getIp() {
        return this.ip;
    }

    public String getDesc() {
        return this.desc;
    }

    public Date getAddTime() {
        return this.addTime;
    }

    /**
     * 是否属于某个地区，注意这个返回的是根据公司维护的数据，而没有真的去查询IP归属地
     * <p>
     * 可以输入类似 广东、广州、北京啥的， 对比的是描述信息中的
     *
     * @param area 地区
     * @return 返回是否包含指定地区
     */
    public boolean belongArea(String area) {

        return this.desc != null && this.desc.contains(area);
    }
}
