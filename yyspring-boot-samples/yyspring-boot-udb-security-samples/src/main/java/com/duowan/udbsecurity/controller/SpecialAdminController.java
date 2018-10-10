package com.duowan.udbsecurity.controller;

import com.duowan.common.web.view.JsonView;
import com.duowan.udb.security.annotations.IgnoredUdbCheck;
import com.duowan.udb.security.annotations.UdbCheck;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/10 13:24
 */
@Controller
@RequestMapping("/admin")
@IgnoredUdbCheck
public class SpecialAdminController {

    /**
     * 需要拦截, 可以使用 UdbCheck 注解覆盖类级别的注解效果
     */
    @RequestMapping("/speccheck")
    @UdbCheck
    public JsonView check() {
        return new JsonView();
    }

    /**
     * 因为本类设置了 IgnoredUdbCheck 注解，所以这个不需要拦截
     */
    @RequestMapping("/specuncheck")
    public JsonView uncheck() {
        return new JsonView();
    }
}
