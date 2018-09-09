package com.duowan.common.innerip;

/**
 * 办公网IP业务接口定义
 *
 * @author Arvin
 */
public interface InnerIpService {

    /**
     * 判断给定的IP是不是办公网的IP
     *
     * @param ip 要检查的IP
     * @return 返回是否办公网IP
     */
    boolean isOfficialIp(String ip);

    /**
     * 获取办公网IP对象
     *
     * @param ip 办公网IP
     * @return 如果该IP是办公网的IP则返回办公网的IP，否则返回null
     */
    OfficialIp getOfficialIp(String ip);

}
