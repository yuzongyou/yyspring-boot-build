package com.duowan.common.utils;

import com.duowan.common.utils.exception.UtilsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

/**
 * @author Arvin
 */
public class EncryptUtil {

    private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

    private EncryptUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * MD5 加密，加密后的密文是大写
     *
     * @param rawString 要加密的字符串
     * @return 返回大写格式的 MD5 字符串
     */
    public static String toMd5UpperCase(String rawString) {
        return encode(rawString, "MD5").toUpperCase();
    }

    /**
     * MD5 加密，加密后的密文是小写
     *
     * @param rawString 要加密的字符串
     * @return 返回小写格式的 MD5 字符串
     */
    public static String toMd5LowerCase(String rawString) {
        return encode(rawString, "MD5").toLowerCase();
    }

    /**
     * 按类型对字符串进行加密并转换成16进制输出</br>
     *
     * @param str  字符串
     * @param type 可加密类型md5, des , sha1
     * @return 加密后的字符串
     */
    private static String encode(String str, String type) {
        try {
            MessageDigest alga = MessageDigest.getInstance(type);
            alga.update(str.getBytes());
            byte[] bytes = alga.digest();
            return byte2hex(bytes);
        } catch (Exception e) {
            logger.warn("加密字符串错误：type=[" + type + "] str=[" + str + "] error=[" + e.getMessage() + "]", e);
        }
        return "";
    }

    /**
     * 将字节数组转换成16进制字符
     *
     * @param bytes 需要转换的字节数组
     * @return 转换后的16进制字符
     */
    private static String byte2hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < bytes.length; n++) {
            String stmp = Integer.toHexString(bytes[n] & 0xff);
            if (stmp.length() == 1) {
                sb.append("0");
            }
            sb.append(stmp);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 进行 HmacSHA1 加密
     *
     * @param key   加密key
     * @param value 要加密的值
     * @return 返回加密后的值
     */
    public static String hmacSHA1(String key, String value) {
        try {

            // 加密密钥
            SecretKeySpec localSecretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
            Mac localMac = Mac.getInstance("HmacSHA1");
            localMac.init(localSecretKeySpec);
            // 加密内容，这里使用时间
            localMac.update(value.getBytes("UTF-8"));

            return byte2hex(localMac.doFinal());
        } catch (Exception e) {
            throw new UtilsException(e);
        }
    }
}
