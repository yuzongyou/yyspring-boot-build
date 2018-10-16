package com.duowan.yyspringboot.autoconfigure.udbpage.annotations;

import com.duowan.udb.sdk.AttrLookupScope;
import com.duowan.udb.sdk.UdbAuthLevel;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * UDB 登录验证状态
 *
 * @author Arvin
 * @since 2018/10/9 18:46
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UdbLoginCheck {

    /**
     * 验证级别
     *
     * @return 验证级别
     */
    @AliasFor("authLevel")
    UdbAuthLevel value() default UdbAuthLevel.LOCAL;

    @AliasFor("value")
    UdbAuthLevel authLevel() default UdbAuthLevel.LOCAL;

    /**
     * 属性搜索路径
     *
     * @return 返回属性搜索路径
     */
    AttrLookupScope[] scopes() default {};
}
