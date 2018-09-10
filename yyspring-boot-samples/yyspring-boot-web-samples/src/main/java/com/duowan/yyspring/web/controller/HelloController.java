package com.duowan.yyspring.web.controller;

import com.duowan.yyspringboot.autoconfigure.web.view.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/10 9:47
 */
@Controller
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("/hello/{name}")
    public JsonView hello(@PathVariable String name, String requestUri) {
        logger.info(requestUri + ": " + name);
        logger.error(requestUri + ": " + name);
        return new JsonView("Hello, " + name + ": " + requestUri);
    }

    @RequestMapping("/testError")
    public JsonView testError() throws Exception {
        throw new Exception("未知错误！");
    }
}

