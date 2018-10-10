package com.duowan.udbsecurity.controller;

import com.duowan.common.web.view.JsonView;
import com.duowan.udb.security.annotations.UdbCheck;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/10 13:26
 */
@Controller
@RequestMapping("/other")
public class OtherController {
    /**
     * 需要拦截的接口, 虽然这个没有配置，但是可以直接使用注解来拦截
     */
    @RequestMapping("/check")
    @UdbCheck
    public JsonView check() {
        return new JsonView();
    }

    /**
     * 不需要需要拦截的接口
     */
    @RequestMapping("/uncheck")
    public JsonView uncheck() {
        return new JsonView();
    }
}
