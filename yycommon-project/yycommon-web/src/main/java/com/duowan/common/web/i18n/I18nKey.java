package com.duowan.common.web.i18n;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/14 15:57
 */
public enum I18nKey {

    /**
     * 系统内部错误
     **/
    INTERNAL_ERROR("i18n.internal.error"),
    ;

    private String key;

    I18nKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
