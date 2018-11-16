package com.duowan.common.web.i18n;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/14 16:20
 */
public class DefaultLangResolver implements LangResolver {

    private String langVarName = "lang";

    public DefaultLangResolver() {
    }

    public DefaultLangResolver(String langVarName) {
        this.langVarName = langVarName;
    }

    public String getLangVarName() {
        return langVarName;
    }

    public void setLangVarName(String langVarName) {
        this.langVarName = langVarName;
    }

    @Override
    public String resolveLang(HttpServletRequest request) {

        String lang = request.getHeader(langVarName);
        if (null == lang || "".equals(lang)) {
            lang = request.getParameter(langVarName);
        }

        return lang;
    }
}
