package com.duowan.udb.sdk;

import java.util.HashMap;
import java.util.Map;

/**
 * UDB 登录结果Code
 *
 * @author Arvin
 * @time 2017/12/4 17:13
 */
public enum RCode {

    SUCCESS(1, "成功；"),
    ERR_ARG(-10001, "参数缺失或其值为空"),
    ERR_TOKEN_EXPIRE_OR_THIRD_INTERNAL(-20302, "token过期或第三方系统内部故障"),
    ERR_TOKEN(-20303, "token错误"),
    ERR_LOGIN_FORBIDDEN(-20304, "登录封禁"),
    ERR_FREQ_LIMIT(-10002, "访问频繁"),
    ERR_INVALID_IP(-10003, "非法IP"),
    ERR_WRITE_COOKIE(-10006, "写cookie失败"),
    ERR_INTERNAL(-10010, "操作失败,稍后重试"),
    ERR_FETCH_ACCOUNT_INFO(-10015, "获取accountinfo失败"),
    ERR_UNKNOWN(-9999, "未知错误");

    private int id;
    private String reason;

    RCode(int id, String reason) {
        this.id = id;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }


    private static final Map<Integer, RCode> MAP = new HashMap<>();

    static {
        RCode[] rCodes = RCode.values();
        for (RCode rCode : rCodes) {
            MAP.put(rCode.getId(), rCode);
        }
    }

    public static RCode get(Integer id) {
        return MAP.get(id);
    }

    public static boolean isSuccess(Integer id) {
        return SUCCESS.equals(MAP.get(id));
    }

    public static String getReason(Integer id) {
        RCode rCode = get(id);
        if (rCode == null) {
            rCode = ERR_UNKNOWN;
        }
        return rCode.getReason();
    }
}
