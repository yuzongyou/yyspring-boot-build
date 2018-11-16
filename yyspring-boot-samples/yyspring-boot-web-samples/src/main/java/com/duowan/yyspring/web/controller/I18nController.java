package com.duowan.yyspring.web.controller;

import com.duowan.common.web.response.JsonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/14 11:17
 */
@RestController
public class I18nController extends BaseController {


    @GetMapping("/i18n")
    public JsonResponse<Object> i18n(HttpServletRequest request) {

        Map<String, Object> paramsMap = new HashMap<>();
        fillRequestParameter(paramsMap, request, true, true);

        return new JsonResponse<>(paramsMap);
    }
}
