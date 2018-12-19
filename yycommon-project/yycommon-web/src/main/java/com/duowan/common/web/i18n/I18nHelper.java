package com.duowan.common.web.i18n;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 国际化支持
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/11/14 14:12
 */
public class I18nHelper {

    private I18nHelper() {
        throw new IllegalStateException("Utility class");
    }

    private static final LangResolver DEFAULT_LANG_RESOLVER = new DefaultLangResolver();

    private static LangResolver langResolver;

    /**
     * 语言 --> 消息映射
     **/
    private static Map<String, Map<String, String>> langMessageMap = new HashMap<>();

    /**
     * 默认的消息映射
     **/
    private static Map<String, String> defaultMessageMap = new HashMap<>();

    public static LangResolver getLangResolver() {

        return langResolver == null ? DEFAULT_LANG_RESOLVER : langResolver;
    }

    public static void setLangResolver(LangResolver langResolver) {
        I18nHelper.langResolver = langResolver;
    }

    /**
     * 对message信息进行消息适配
     *
     * @param lang    语言
     * @param message 要适配语言的参数
     * @param params  参数
     * @return 返回适配后的信息
     */
    public static String adapter(String lang, String message, Object[] params) {
        String finalMsg = adapter(lang, message);
        if (null != finalMsg && finalMsg.length() > 1 && null != params && params.length > 0) {
            return String.format(finalMsg, params);
        }
        return finalMsg;
    }

    /**
     * 对message信息进行消息适配
     *
     * @param lang    语言
     * @param message 要适配语言的参数
     * @return 返回适配后的信息
     */
    public static String adapter(String lang, String message) {
        return message;
    }

    /**
     * 对message信息进行消息适配
     *
     * @param request 当前请求，用于计算客户端的语言
     * @param message 要适配语言的参数
     * @param params  参数
     * @return 返回适配后的信息
     */
    public static String adapter(HttpServletRequest request, String message, Object[] params) {
        return adapter(getLangResolver().resolveLang(request), message, params);
    }

    /**
     * 对message信息进行消息适配
     *
     * @param request 当前请求，用于计算客户端的语言
     * @param message 要适配语言的参数
     * @return 返回适配后的信息
     */
    public static String adapter(HttpServletRequest request, String message) {
        return adapter(getLangResolver().resolveLang(request), message);
    }
}
