package com.duowan.udbsecurity.controller;

import com.duowan.common.web.view.JsonView;
import com.duowan.udb.sdk.UdbAuthLevel;
import com.duowan.yyspringboot.autoconfigure.udbpage.annotations.UdbLoginCheck;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/29 19:28
 */
@Controller
public class UdbController {

    @RequestMapping("/udbpages")
    public JsonView udbpages(String strongPassport, // 远程校验登录态成功则注入
                             Long strongYyuid,  // 验证校验登录态成功则注入
                             String weakPassport,   // 本地Cookie校验通过则注入
                             Long weakYyuid,  // 本地Cookie校验通过则注入
                             @UdbLoginCheck(UdbAuthLevel.LOCAL) String loginPassport, // 直接指定验证级别，使用本地验证，可以用weakPassport 代替
                             @UdbLoginCheck(UdbAuthLevel.STRONG) Long loginYyuid, // 直接指定使用远程验证，可以使用 strongYyuid 代替
                             Boolean udbStrongLogin, Boolean udbWeakLogin,
                             String decryptPartnerInfo, String decryptOauthCookie) {

        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("strongPassport", strongPassport);
        dataMap.put("strongYyuid", strongYyuid);
        dataMap.put("weakPassport", weakPassport);
        dataMap.put("weakYyuid", weakYyuid);
        dataMap.put("loginPassport", loginPassport);
        dataMap.put("loginYyuid", loginYyuid);
        dataMap.put("udbStrongLogin", udbStrongLogin);
        dataMap.put("udbWeakLogin", udbWeakLogin);
        dataMap.put("decryptPartnerInfo", decryptPartnerInfo);
        dataMap.put("decryptOauthCookie", decryptOauthCookie);

        return new JsonView(dataMap);
    }

    @RequestMapping("/redirect")
    public ModelAndView redirect(String url) {
        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping("/redirect2")
    public void redirect2(String url, HttpServletResponse response) {
        response.setStatus(302);
        response.setHeader("Location", url);
    }

    @RequestMapping("/redirect3")
    public void redirect3(String url, HttpServletResponse response) throws IOException {
        response.sendRedirect(url);
    }
}
