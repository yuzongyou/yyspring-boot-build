package com.duowan.yyspringboot.autoconfigure;

import com.duowan.yyspring.boot.AppContext;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/1 20:25
 */
public class BaseTest {

    static {
        System.setProperty("PROJECTNO", "yyspring-boot-autoconfigure");
        System.setProperty("ENV", AppContext.ENV_DEV);
    }
}
