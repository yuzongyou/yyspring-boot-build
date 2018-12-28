package com.duowan.yyspring.web.controller;

//import com.duowan.common.ipowner.IpOwnerService;
//import com.duowan.common.jdbc.Jdbc;

import com.duowan.common.web.response.JsonResponse;
import com.duowan.common.web.view.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/10 9:47
 */
@Controller
@RequestMapping("/test")
public class HelloController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

//    @Autowired
//    private IpOwnerService ipOwnerService;
//
//    @Autowired
//    private Jdbc jdbc;

////    @RequestMapping("/hello/{name}")
//    public JsonView hello(@PathVariable String name, String requestUri) {
//        logger.info(requestUri + ": " + name);
//        logger.error(requestUri + ": " + name);
//        return new JsonView("Hello, " + name + ": " + requestUri);
//    }
//
////    @RequestMapping("/testError")
//    public JsonView testError() throws Exception {
//        throw new Exception("未知错误！");
//    }

    @RequestMapping
    public JsonView hello1(String name, String requestUri) {
        logger.info(requestUri + ": " + name);
        logger.error(requestUri + ": " + name);
        return new JsonView("Hello1, " + name + ": " + requestUri);
    }

    @RequestMapping("hello2.do")
    public JsonView hello2(String name, String requestUri) {
        logger.info(requestUri + ": " + name);
        logger.error(requestUri + ": " + name);
        return new JsonView("Hello1, " + name + ": " + requestUri);
    }

//    @GetMapping
//    public JsonView hello2(@PathVariable String name, String requestUri) {
//        logger.info(requestUri + ": " + name);
//        logger.error(requestUri + ": " + name);
//        return new JsonView("Hello2, " + name + ": " + requestUri);
//    }
//
//    @PostMapping
//    public JsonView hello3(@PathVariable String name, String requestUri) {
//        logger.info(requestUri + ": " + name);
//        logger.error(requestUri + ": " + name);
//        return new JsonView("3, " + name + ": " + requestUri);
//    }

    @RequestMapping("/requestBody")
    public JsonView<Object> requestBody(HttpServletRequest request, @RequestBody Map<String, Object> params) {

        Map<String, Object> paramsMap = new HashMap<>();
        fillRequestParameter(paramsMap, request, true, true);
        paramsMap.put("requestBody", params);

        return new JsonView<>(paramsMap);
    }

    @RequestMapping("/requestBody2")
    public JsonResponse<Object> requestBody2(HttpServletRequest request, @RequestBody Map<String, Object> params) {

        Map<String, Object> paramsMap = new HashMap<>();
        fillRequestParameter(paramsMap, request, true, true);
        paramsMap.put("requestBody", params);

        return new JsonResponse<>(paramsMap);
    }

    @RequestMapping("/cal")
    @ResponseBody
    public JsonResponse<Object> cal(int a, int b) {
        return new JsonResponse<>(a / b);
    }

}

