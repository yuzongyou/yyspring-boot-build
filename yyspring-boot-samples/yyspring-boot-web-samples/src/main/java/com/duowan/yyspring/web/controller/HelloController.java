package com.duowan.yyspring.web.controller;

import com.duowan.common.ipowner.IpOwnerService;
import com.duowan.common.jdbc.Jdbc;
import com.duowan.common.web.view.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IpOwnerService ipOwnerService;

    @Autowired
    private Jdbc jdbc;

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

