package com.duowan.yyspringboot.autoconfigure.web.view;

import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.ConvertUtil;
import com.duowan.common.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Arvin
 */
public abstract class ViewUtil {

    /**
     * JSONP 回调函数名称列表
     */
    private static List<String> jsonpCallbackVars = CommonUtil.splitAsStringList(AppContext.getAppProperty("view.jsonp.callback.vars", "callback"), null);

    /**
     * 时间格式变量
     */
    private static List<String> dateFormatVars = CommonUtil.splitAsStringList(AppContext.getAppProperty("view.json.dateFormat.vars", "dateFormat"), null);

    /**
     * JavaScript 变量列表
     */
    private static List<String> javascriptVars = CommonUtil.splitAsStringList(AppContext.getAppProperty("view.javascript.vars", "var"), null);

    private static class Holder {

        /**
         * AjaxView, JsonView 输出结果使用的 状态码名称
         */
        static final String AJAX_STATUS_CODE_NAME = ConvertUtil.toString(AppContext.getAppProperty("view.ajax.status.code.name", "status"));
    }

    /**
     * Javascript 变量名称正则表达式
     */
    private static String JAVASCRIPT_VAR_REGEX = "(?i)^[a-z][a-zA-Z0-9_\\.]*$";

    public static String[] getJsonpCallbackVars() {
        if (jsonpCallbackVars == null) {
            jsonpCallbackVars = CommonUtil.splitAsStringList(AppContext.getAppProperty("view.jsonp.callback.vars", "callback"), null);
        }
        if (jsonpCallbackVars.isEmpty()) {
            return new String[]{"callback"};
        }
        return jsonpCallbackVars.toArray(new String[jsonpCallbackVars.size()]);
    }

    public static String[] getJavascriptVars() {
        if (javascriptVars == null) {
            javascriptVars = CommonUtil.splitAsStringList(AppContext.getAppProperty("view.javascript.vars", "var"), null);
        }
        if (javascriptVars.isEmpty()) {
            return new String[]{"var"};
        }
        return javascriptVars.toArray(new String[javascriptVars.size()]);
    }

    public static String getAjaxStatusCodeName() {
        return Holder.AJAX_STATUS_CODE_NAME;
    }

    /**
     * 搜索指定参数名称的值
     *
     * @param request 请求
     * @param vars    参数名称劣币哦啊
     * @return 返回第一个不为空的值， 如果没有则返回null
     */
    private static String lookupByVars(HttpServletRequest request, List<String> vars) {
        if (request == null || null == vars || vars.isEmpty()) {
            return null;
        }

        for (String var : vars) {
            String value = request.getParameter(var);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }

    /**
     * <pre>
     * 搜索 jsonp 回调函数值, 回调函数的名称可以在 application-[env].properties 文件中配置变量 view.jsonp.callback.vars， 中间使用英文逗号分割
     *
     * 默认是 callback
     *
     * </pre>
     *
     * @param request 请求
     * @return 如果有的话就返回，没有就返回null， 如果存在但是名称不符合规范则返回null
     */
    public static String lookupJsonpCallback(HttpServletRequest request) {

        String callback = lookupByVars(request, jsonpCallbackVars);

        if (StringUtils.isNotBlank(callback)) {
            if (callback.trim().matches(JAVASCRIPT_VAR_REGEX)) {
                return callback.trim();
            }
        }

        return null;
    }

    /**
     * 获取请求的所有请求变量名称
     *
     * @param request 请求
     * @return 返回一个非null集合
     */
    private static Set<String> getParamNames(HttpServletRequest request) {
        Set<String> names = new HashSet<>();

        if (null == request) {
            return names;
        }

        Enumeration<String> enumeration = request.getParameterNames();

        while (enumeration.hasMoreElements()) {
            names.add(enumeration.nextElement());
        }

        return names;
    }

    /**
     * <pre>
     * 获取时间格式匹配规则，请求变量名称默认为 dateFormat，
     *
     * 可在 application-[env].properties 文件中配置变量 view.json.dateFormat.vars， 中间使用英文逗号分割
     *
     * </pre>
     *
     * @param request 请求
     * @return 如果有的话就返回，没有就返回null， 如果存在但是名称不符合规范则抛出异常
     */
    public static String lookupDateFormatPattern(HttpServletRequest request) {
        if (request == null || null == dateFormatVars || dateFormatVars.isEmpty()) {
            return null;
        }

        Set<String> paramNames = getParamNames(request);

        String defaultDateFormat = "yyyy-MM-dd HH:mm:ss";

        for (String var : dateFormatVars) {
            if (paramNames.contains(var)) {
                String value = request.getParameter(var);
                if (StringUtils.isBlank(value)) {
                    return defaultDateFormat;
                }
                return value;
            }
        }
        return null;
    }

    /**
     * <pre>
     * 获取 javascript 输出变量名称， 名称可以在 application-[env].properties 文件中配置变量 view.javascript.vars， 中间使用英文逗号分割
     *
     * 默认是 var
     * </pre>
     *
     * @param request 请求
     * @return 返回变量名称，如果
     */
    public static String lookupJavascriptVar(HttpServletRequest request) {
        String javascriptVar = lookupByVars(request, javascriptVars);

        if (StringUtils.isNotBlank(javascriptVar)) {
            if (javascriptVar.trim().matches(JAVASCRIPT_VAR_REGEX)) {
                return javascriptVar.trim();
            }
        }

        return null;
    }

    /**
     * 获取 ajax 数据结果字符串
     *
     * @param data    数据对象
     * @param request 请求
     * @return 返回一个字符串
     */
    public static String getAjaxOutputString(Object data, HttpServletRequest request) {

        if (data instanceof CharSequence) {
            return getAjaxOutputText((String) data, request);
        }

        String dateFormat = lookupDateFormatPattern(request);

        String body;

        if (StringUtils.isBlank(dateFormat)) {
            body = JsonUtil.toJson(data);
        } else {
            body = JsonUtil.toJsonWithDataFormat(data, dateFormat);
        }

        String jsonpCallback = lookupJsonpCallback(request);

        if (StringUtils.isNotBlank(jsonpCallback)) {
            return jsonpCallback + "(" + body + ");";
        }

        String javascriptVar = lookupJavascriptVar(request);

        if (StringUtils.isNotBlank(javascriptVar)) {
            return "var " + javascriptVar + "=" + body + ";";
        }

        return body;
    }

    /**
     * 获取 Ajax 字符串文本
     *
     * @param text    文本字符串
     * @param request 请求
     * @return
     */
    public static String getAjaxOutputText(String text, HttpServletRequest request) {

        String jsonpCallback = lookupJsonpCallback(request);

        if (StringUtils.isNotBlank(jsonpCallback)) {
            return jsonpCallback + "(" + JsonUtil.toJson(text) + ");";
        }

        String javascriptVar = lookupJavascriptVar(request);

        if (StringUtils.isNotBlank(javascriptVar)) {
            return "var " + javascriptVar + "=" + JsonUtil.toJson(text) + ";";
        }

        return text == null ? "null" : text;
    }
}
