package com.duowan.udbsecurity.controller;

import com.duowan.common.web.view.JsonView;
import com.duowan.udb.security.annotations.IgnoredUdbCheck;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/10 13:21
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    /**
     * 需要拦截的接口
     */
    @RequestMapping("/check")
    public JsonView check() {
        return new JsonView();
    }

    /**
     * 不需要需要拦截的接口，使用 IgnoredUdbCheck 注解
     */
    @RequestMapping("/uncheck")
    @IgnoredUdbCheck
    public JsonView uncheck() {
        return new JsonView();
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

}
