package com.duowan.yyspringcloud.msauth;

import com.duowan.common.utils.EncryptUtil;
import com.duowan.common.utils.StringUtil;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 10:02
 */
public class Signer {

    /**
     * 参数签名
     *
     * @param appId       appid
     * @param secret      签名key
     * @param liveSeconds 存活时间，单位是秒
     * @return 签名字符串
     */
    public static String sign(String appId, String secret, long liveSeconds) {

        long expireTime;
        if (liveSeconds <= 0) {
            expireTime = -1;
        } else {
            expireTime = System.currentTimeMillis() + liveSeconds * 1000;
        }

        return expireTime + "|" + EncryptUtil.toMd5UpperCase(expireTime + "|" + appId + "|" + secret);
    }

    public static boolean isValid(String appId, String secret, String reqSign) {

        if (StringUtil.isBlank(reqSign)) {
            return false;
        }
        int index = reqSign.indexOf('|');
        long expireTime = Long.parseLong(reqSign.substring(0, index));
        if (expireTime > 0 && expireTime < System.currentTimeMillis()) {
            return false;
        }
        String realSign = expireTime + "|" + EncryptUtil.toMd5UpperCase(expireTime + "|" + appId + "|" + secret);
        return realSign.equals(reqSign);
    }

    public static void main(String[] args) throws InterruptedException {
        String appId = "admin";
        String secret = "123456";
        String sign = Signer.sign(appId, secret, 1);

        Thread.sleep(1500);
        System.out.println(isValid(appId, secret, sign));

    }
}
