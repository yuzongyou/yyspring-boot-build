package com.duowan.common.ipowner.seeker;

/**
 * IP Seeker 提供者
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/7/27 16:41
 */
public interface IpSeekerProvider {

    /**
     * 获取一个 IP Seeker
     *
     * @return 返回 IP seeker
     */
    IpSeeker getSeeker();
}
