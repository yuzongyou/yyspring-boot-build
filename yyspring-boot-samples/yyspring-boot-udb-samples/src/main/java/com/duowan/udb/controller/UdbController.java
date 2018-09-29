package com.duowan.udb.controller;

import com.duowan.common.web.view.JsonView;
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
                             String weakPassport, String weakYyuid,
                             Boolean udbStrongLogin, Boolean udbWeakLogin,
                             String decryptPartnerInfo, String decryptOauthCookie) {

        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("strongPassport", strongPassport);
        dataMap.put("strongYyuid", strongYyuid);
        dataMap.put("weakPassport", weakPassport);
        dataMap.put("weakYyuid", weakYyuid);
        dataMap.put("udbStrongLogin", udbStrongLogin);
        dataMap.put("udbWeakLogin", udbWeakLogin);
        dataMap.put("decryptPartnerInfo", decryptPartnerInfo);
        dataMap.put("decryptOauthCookie", decryptOauthCookie);

        return new JsonView(dataMap);
    }
}
