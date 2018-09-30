package com.duowan.udb.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.duowan.common.exception.CodeException;
import com.duowan.common.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * UDB 工具类
 *
 * @author Arvin
 * @time 2018/3/30 18:21
 */
public class UdbClient {

    private static final Log logger = LogFactory.getLog(UdbClient.class);

    /**
     * 根据微信 UnionId 获取用户 yyuid
     */
    private static final String GET_YYUID_BY_WX_UNIONID_URL_TMPL = "https://thirdlogin.yy.com/message/yy_distraction_userinfo.do?miacct=%s&timestmap=%s&sign=%s&appId=%s&type=all";

    private static final int SUCCESS_CODE = 0;

    /**
     * <pre>
     * 根据微信 unionId 查询指定渠道配置的 yyuid
     *
     * http://backendtech.yy.com/article.html?id=305#oauth_login
     *
     * 请求参数：
     * 1.miacct: 第三方用户信息，格式：source|third_sub_sys|UID,如:qq|weibo|B60BD875F9001DF7055FE3941A2FFF01
     * 2.timestmap:验签的时间戳,如: 12342322223
     * 3.sign:需要验证的签名,通过HmacSHA1进行加密miacct+timestmap字符串，加密的Key为appKey
     * 4.appId:要查询的渠道的AppId
     *
     * 	返回数据：
     *  {"code":"0","desc":"succ","data":{"uid":"xxx","yyuid":"xxxx"},"obj":null}
     *  {"code":"1002","desc":"Validate sign is fail!","data":null,"obj":null}
     * 1.yyuid:当前第三方用户对应的YYUID
     * 2.uid:当前系三方用户的UID, 其实就是ubionId
     * </pre>
     *
     * @param udbAppId    udb分配的 appId
     * @param udbAppKey   udb 分配的对应UDB appId 的key
     * @param source      渠道来源
     * @param thirdSubSys 子渠道
     * @param unionId     微信 unionId
     * @return 如果不存在则返回 <= 0 的数字
     */
    public static long getYyuidByWeixinUnionId(String udbAppId, String udbAppKey, String source, String thirdSubSys, String unionId) {
        String miacct = source + "|" + thirdSubSys + "|" + unionId;
        String timestamp = String.valueOf(System.currentTimeMillis());
        String sign = EncryptUtil.hmacSHA1(udbAppKey, miacct + timestamp).toLowerCase();

        String url = String.format(GET_YYUID_BY_WX_UNIONID_URL_TMPL, miacct, timestamp, sign, udbAppId);

        String responseText = null;
        try {

            responseText = HttpUtil.doGet(url);

            AssertUtil.assertNotBlank(responseText, "unionId -> yyuid 异常");

            JSONObject jsonObject = JSON.parseObject(responseText);
            int code = jsonObject.getIntValue("code");
            if (SUCCESS_CODE == code) {
                JSONObject obj = (JSONObject) jsonObject.get("data");
                return obj.getLongValue("yyuid");
            } else {
                String error = jsonObject.getString("desc");
                throw new CodeException(code, error);
            }
        } finally {
            logger.info("unionId => yyuid: udbAppId=" + udbAppId + ", udbAppKey=" +
                    udbAppKey + ", source=" +
                    source + ", thirdSubSys=" +
                    thirdSubSys + ", unionId=" +
                    unionId + ", resp=" + StringUtils.trim(responseText) + ", url=" + url);
        }

    }

    /**
     * UDB 登录使用的 URL
     */
    private static final String UDB_LOGIN_URL = "https://thirdlogin.yy.com/open/xtokenlogin.do";

    /**
     * 使用微信一次性 code 登录
     */
    private static final String OAUTH_TYPE_ACCESS_CODE = "0";
    /**
     * 使用微信 accessToken 登录
     */
    private static final String OAUTH_TYPE_ACCESS_TOKEN = "1";

    /**
     * 使用微信 AccessToken 进行登录
     *
     * @param accessToken 微信 accessToken
     * @param udbAppId    udbAppId
     * @param wxAppId     微信 appId
     * @param source      渠道来源
     * @param thirdSubSys 第三方子渠道
     * @return 如果登录成功则返回 LoginResult，否则抛出异常
     */
    public static LoginResult loginByWeixinAccessToken(String accessToken, String udbAppId, String wxAppId, String source, String thirdSubSys) {

        String udbLoginUrl = buildUdbXtokenLoginUrl(OAUTH_TYPE_ACCESS_TOKEN, accessToken, udbAppId, wxAppId, source, thirdSubSys);

        logger.info("LOGIN-WX-UDB-TOKEN: " + udbLoginUrl);

        String response = HttpUtil.doPost(udbLoginUrl);

        logger.info("LOGIN-WX-UDB-TOKEN-RESP: accessToken=" + accessToken + ", " + response);

        return responseToLoginResult(accessToken, response);

    }

    private static LoginResult responseToLoginResult(String codeOrToken, String response) {

        Map<String, Object> map = JsonUtil.toObjectMap(response);

        RCode rcode = RCode.get(ConvertUtil.toInteger(map.get("rcode"), RCode.ERR_ARG.getId()));

        boolean isSuccess = RCode.SUCCESS.equals(rcode);

        if (!isSuccess) {
            String errorMsg = String.valueOf(map.get("msg"));
            if (StringUtils.isBlank(errorMsg)) {
                errorMsg = rcode.getReason();
            }
            throw new IllegalArgumentException(errorMsg);
        }

        LoginResult loginResult = new LoginResult();
        loginResult.setId(codeOrToken);
        loginResult.setCkList(convertUdbLoginCookieList(map));

        return loginResult;
    }

    private static final Set<String> IGNORE_UDB_COOKIE_NAMES = new HashSet<>(Arrays.asList(
            "sign", "tl", "rcode", "msg", "appid"
    ));

    /**
     * 转成 UDB 的 Cookie
     *
     * @param map 结果 MAP
     * @return
     */
    private static List<CK> convertUdbLoginCookieList(Map<String, Object> map) {
        List<CK> ckList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String ckName = entry.getKey();
            if (!IGNORE_UDB_COOKIE_NAMES.contains(ckName)) {
                String ckValue = String.valueOf(entry.getValue());
                if (null != ckValue) {
                    ckValue = ckValue.replaceFirst("^\"+", "").replaceFirst("\"+$", "");
                    if (ckValue.indexOf("http") == 0) {
                        ckValue = UrlUtil.decodeParamValue(ckValue);
                    } else {
                        ckValue = UrlUtil.encodeParamValue(ckValue);
                    }
                }
                ckList.add(new CK(ckName, ckValue));
            }
        }

        return ckList;
    }


    /**
     * 构造UDB的微信授权登录地址
     *
     * @param oauthType   oauth 认证类型，使用 code 还是 ACCESS_TOKEN
     * @param codeOrToken code 或者是 AccessToken
     * @param udbAppId    udb appId
     * @param wxAppId     微信 appid
     * @param source      渠道来源
     * @param thirdSubSys 第三方子渠道标识
     * @return 返回登录url
     */
    private static String buildUdbXtokenLoginUrl(String oauthType, String codeOrToken, String udbAppId, String wxAppId, String source, String thirdSubSys) {
        Map<String, String> paramMap = new HashMap<String, String>(7);
        paramMap.put("source", source);
        paramMap.put("third_sub_sys", thirdSubSys);
        paramMap.put("udb_appid", udbAppId);
        paramMap.put("tokenid", codeOrToken);
        paramMap.put("third_appkey", wxAppId);
        paramMap.put("oauth_url", "");
        paramMap.put("oauth_type", StringUtils.isBlank(oauthType) ? OAUTH_TYPE_ACCESS_CODE : OAUTH_TYPE_ACCESS_TOKEN);

        String urlParam = UrlUtil.toUrlParamsString(paramMap, true, false);
        String url = UDB_LOGIN_URL + "?" + urlParam;
        return url;
    }

    /**
     * 微信绑定YY帐号地址URL
     */
    private static final String WX_BIND_YY_URL_TMPL = "https://thirdlogin.yy.com/wx/authorize.do?reqId=%s&appId=%s&openId=%s&time=%s&sign=%s&redirect_uri=%s";

    /**
     * 构造一个微信绑定YY帐号的绑定地址
     *
     * @param reqId        请求唯一标识
     * @param udbAppId     udbAppId
     * @param udbAppSecret 互联KEY
     * @param unionId      unionId
     * @param jumpUrl      跳转地址
     * @return 返回一个URL
     */
    public static String buildWeixinBindYYUrl(String reqId, String udbAppId, String udbAppSecret, String unionId, String jumpUrl) {

        // 互联key,通过/message/getthirdkey.do 获取
        String aesKey = getAesEncryptKey(udbAppId);

        String openId = AESHelper.encrypt(unionId, aesKey);

        String redirectUrl = UrlUtil.decodeUrl(jumpUrl);
        String time = String.valueOf(System.currentTimeMillis());

        // aes.encrypt(“appid|openid|time|redirect_uri”，key)
        String data = udbAppId + "|" + openId + "|" + time + "|" + redirectUrl;
        // 使用udb的业务互联key，固定值
        String sign = AESHelper.encrypt(data, udbAppSecret);

        return String.format(WX_BIND_YY_URL_TMPL, reqId, udbAppId, openId, time, sign, UrlUtil.encodeUrl(redirectUrl));
    }

    /**
     * 获取
     */
    private static final String GET_AES_KEY_URL_TMPL = "https://thirdlogin.yy.com/message/getthirdkey.do?appid=%s&type=new";

    /**
     * 不需要关系并发和同步问题
     */
    private static final Map<String, UdbAesKey> UDB_AES_KEY_MAP = new HashMap<>();

    /**
     * 获取 AES 加密使用的key， 格式为：
     * 格式:新key;旧key;新key过期时间;新key开始时间
     * 841b0b81-f847-4d6e-887d-dcad8601bf02;6ce89d4e-a57e-481c-9d97-7c6007b15528;1522918800000;1522314000000
     * <p>
     * 实际是 7 天有效
     *
     * @param udbAppId udb 的 appid
     * @return
     */
    public static String getAesEncryptKey(String udbAppId) {

        UdbAesKey udbAesKey = UDB_AES_KEY_MAP.get(udbAppId);

        if (udbAesKey == null || udbAesKey.isExpired()) {

            String url = String.format(GET_AES_KEY_URL_TMPL, udbAppId);

            logger.info("执行远程查询UDB AESKEY， appid=" + udbAppId + ", url=" + url);

            String responseText = HttpUtil.doGet(url);

            udbAesKey = responseToUdbAesKey(udbAppId, responseText);

            UDB_AES_KEY_MAP.put(udbAppId, udbAesKey);

            return udbAesKey.getNewkey();
        }

        return udbAesKey.getNewkey();

    }

    private static UdbAesKey responseToUdbAesKey(String udbAppId, String responseText) {
        UdbAesKey udbAesKey = new UdbAesKey();
        AssertUtil.assertNotBlank(responseText, "获取AES加密KEY失败：" + udbAppId);

        String[] array = responseText.split(";");

        AssertUtil.assertTrue(array.length == 4, "获取AES加密KEY失败：" + udbAppId + ", 结果不正确！");

        String newkey = array[0];
        String oldkey = array[1];
        long expireTime = ConvertUtil.toLong(array[2]);

        udbAesKey.setAppId(udbAppId);
        udbAesKey.setExpireTime(expireTime);
        udbAesKey.setNewkey(newkey);
        udbAesKey.setOldkey(oldkey);

        return udbAesKey;
    }

}
