package com.duowan.wxmpsdk.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.HttpUtil;
import com.duowan.wxmpsdk.exception.ExceptionCode;
import com.duowan.wxmpsdk.exception.WxmpException;
import com.duowan.wxmpsdk.model.Code2Session;
import com.duowan.wxmpsdk.util.WxmpDecryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/30 9:01
 */
public class WxmpClient {

    private WxmpClient() {
        throw new IllegalStateException("Utility class");
    }

    private static final String CODE_TO_SESSION_URL = " https://api.weixin.qq.com/sns/jscode2session";

    private static final Logger LOGGER = LoggerFactory.getLogger(WxmpClient.class);

    /**
     * 使用一次性微信小程序 code 换取会话信息
     *
     * @param appid  微信小程序颁发的 appid
     * @param secret 微信小程序颁发的 secret
     * @param code   一次性登录 Code
     * @return 返回
     * @throws WxmpException 发生任何错误将包装成本异常抛出
     */
    public static Code2Session code2Session(String appid, String secret, String code) {

        String reqUrl = CODE_TO_SESSION_URL + "?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";

        LOGGER.info("code2Session: reqUrl={}", reqUrl);

        String responseText = null;
        try {
            responseText = HttpUtil.doGet(reqUrl);

            LOGGER.info("code2Session Resp: {}", responseText);
            JSONObject jsonObject = JSON.parseObject(responseText);

            if (jsonObject.containsKey(RetField.SESSION_KEY)) {
                return new Code2Session(jsonObject.getString(RetField.OPENID),
                        jsonObject.getString(RetField.UNIONID),
                        jsonObject.getString(RetField.SESSION_KEY));
            } else {
                String errorMsg = jsonObject.getString(RetField.ERROR_MSG);
                if (StringUtils.isBlank(errorMsg)) {
                    errorMsg = "小程序code无法获取session";
                }
                throw new WxmpException(errorMsg);
            }

        } catch (Exception e) {
            LOGGER.warn("code2Session 失败: response: {}, error={}", responseText, e.getMessage());
            throw new WxmpException(ExceptionCode.ERROR_CODE_TO_SESSION, e);
        }
    }

    /**
     * 进行微信小程序的数据解密
     *
     * @param encryptText    加密数据的原始文本
     * @param ivText         初始向量的文本
     * @param sessionKeyText 会话key的文本
     * @return 返回解密后的文本信息
     * @throws WxmpException 发生任何错误将包装成本异常抛出
     */
    public static String decryptData(String encryptText, String ivText, String sessionKeyText) {
        AssertUtil.assertNotBlank(encryptText, "要解密的数据为空！");
        AssertUtil.assertNotBlank(ivText, "要解密的初始向量为空！");
        AssertUtil.assertNotBlank(sessionKeyText, "要解密的会话key为空！");

        return WxmpDecryptUtil.decrypt(encryptText, ivText, sessionKeyText);
    }
}
