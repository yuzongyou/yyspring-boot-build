package com.duowan.yyspringcloud.msauth.web;

import com.duowan.common.utils.StringUtil;
import com.duowan.yyspringcloud.msauth.Constants;
import com.duowan.yyspringcloud.msauth.Signer;
import com.duowan.yyspringcloud.msauth.app.App;
import com.duowan.yyspringcloud.msauth.app.AppReader;
import com.duowan.yyspringcloud.msauth.exception.EmptyAppReaderException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/17 20:31
 */
public class Md5SignSecurityValidator implements SecurityValidator {

    private final AppReader appReader;

    public Md5SignSecurityValidator(AppReader appReader) {
        if (null == appReader) {
            throw new EmptyAppReaderException();
        }
        this.appReader = appReader;
    }

    @Override
    public SecurityCode validate(String token, HttpServletRequest request, Object handler) {

        if (StringUtil.isBlank(token)) {
            return SecurityCode.SC_UNAUTHORIZED;
        }

        // 解析 微服务id
        int index = token.indexOf(Constants.TOKEN_APPID_SEPARATOR);
        if (index < 1) {
            return SecurityCode.SC_UNAUTHORIZED;
        }
        String appId = token.substring(0, index);
        String sign = token.substring(index + 1);

        if (StringUtil.isAnyBlank(appId, sign)) {
            return SecurityCode.SC_UNAUTHORIZED;
        }

        // 计算签名
        App app = appReader.read(appId);
        if (null == app || StringUtil.isAnyBlank(app.getAppId(), app.getSecretKey())) {
            return SecurityCode.SC_UNAUTHORIZED;
        }
        if (Signer.isValid(appId, app.getSecretKey(), sign)) {
            return SecurityCode.SC_OK;
        }
        return SecurityCode.SC_UNAUTHORIZED;
    }
}
