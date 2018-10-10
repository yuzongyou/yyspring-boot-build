package com.duowan.udb.controller;

import com.duowan.common.web.view.JsonView;
import com.duowan.udb.sdk.UdbAuthLevel;
import com.duowan.yyspringboot.autoconfigure.udbpage.annotations.UdbLoginCheck;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public JsonView udbpages(String strongPassport, Long strongYyuid,
                             String weakPassport, Long weakYyuid,
                             @UdbLoginCheck(UdbAuthLevel.LOCAL) String loginPassport, @UdbLoginCheck(UdbAuthLevel.STRONG) Long loginYyuid,
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
}
