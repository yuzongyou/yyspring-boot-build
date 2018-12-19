package com.duowan.wxmpsdk.util;

import com.duowan.wxmpsdk.exception.WxmpException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;

/**
 * 微信小晨旭数据解密工具类
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/29 17:27
 */
public class WxmpDecryptUtil {

    private WxmpDecryptUtil() {
        throw new IllegalStateException("Utility class");
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 解密数据
     *
     * @param encryptText    被加密过的数据
     * @param ivText         初始向量
     * @param sessionKeyText 会话key
     * @return 返回解密后的文本信息
     * @throws WxmpException 发生任何错误将包装成本异常抛出
     */
    public static String decrypt(String encryptText, String ivText, String sessionKeyText) {

        try {
            byte[] encryptData = Base64.decode(encryptText);
            byte[] ivData = Base64.decode(ivText);
            byte[] sessionKey = Base64.decode(sessionKeyText);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key sKeySpec = new SecretKeySpec(sessionKey, "AES");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivData));
            // 初始化
            byte[] result = cipher.doFinal(encryptData);

            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new WxmpException(e);
        }
    }

    private static AlgorithmParameters generateIV(byte[] iv) {
        try {
            AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
            params.init(new IvParameterSpec(iv));
            return params;
        } catch (Exception e) {
            throw new WxmpException(e);
        }
    }
}
