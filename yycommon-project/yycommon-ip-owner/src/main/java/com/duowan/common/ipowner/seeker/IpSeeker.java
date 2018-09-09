package com.duowan.common.ipowner.seeker;

/**
 * IP 搜索
 *
 * @author Arvin
 */
public interface IpSeeker {

    /**
     * 搜索IP并且以数组返回各列信息
     *
     * @param ip IP
     * @return 返回结果数组
     */
    String[] find(String ip);
}
