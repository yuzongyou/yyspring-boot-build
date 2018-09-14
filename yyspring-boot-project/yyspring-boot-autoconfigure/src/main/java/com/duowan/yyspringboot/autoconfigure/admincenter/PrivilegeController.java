package com.duowan.yyspringboot.autoconfigure.admincenter;

import com.duowan.common.admincenter.annotations.NoPrivilege;
import com.duowan.common.admincenter.model.Privilege;
import com.duowan.common.admincenter.service.AdmincenterService;
import com.duowan.common.web.view.JsonView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/27 16:28
 */
@Controller
public class PrivilegeController {

    private String productId;

    private String logoutUrl;

    private String productName;

    @Autowired
    private AdmincenterService admincenterService;

    public PrivilegeController(String productId, String logoutUrl, String productName) {
        this.productId = productId;
        this.logoutUrl = logoutUrl;
        this.productName = productName;
    }

    @RequestMapping("/admin/privileges.do")
    @NoPrivilege
    public JsonView privileges(String weakPassport, HttpServletRequest request, HttpServletResponse response) {

        if (StringUtils.isBlank(weakPassport)) {
            return new JsonView();
        }

        Map<String, Object> dataMap = new HashMap<>();

        fillUserMap(dataMap, weakPassport);

        fillProductMap(dataMap);

        List<Privilege> privileges = admincenterService.getPrivileges(weakPassport, request);
        fillPrivilegeMap(dataMap, privileges);

        return new JsonView(dataMap);
    }

    private void fillPrivilegeMap(Map<String, Object> dataMap, List<Privilege> privileges) {

        List<Map<String, Object>> privilegeList = new ArrayList<>();

        if (null != privileges && !privileges.isEmpty()) {

            for (Privilege privilege : privileges) {
                Map<String, Object> map = new HashMap<>();
                Privilege parent = privilege.getParent();
                String parentId = parent == null || StringUtils.isBlank(parent.getPrivilegeId()) ? "root" : parent.getPrivilegeId();
                map.put("parentPrivilegeId", parentId);
                map.put("privilegeId", privilege.getPrivilegeId());
                map.put("privilegeName", privilege.getName());
                map.put("productId", productId);
                map.put("showMenu", privilege.isShowMenu());
                map.put("cssClass", privilege.getCssClass());
                map.put("ext", privilege.getExt());

                String firstUrl = privilege.getFirstUrl();
                if (StringUtils.isNotBlank(firstUrl)) {
                    map.put("privilegeUrl", firstUrl);
                }

                privilegeList.add(map);
            }

        }

        dataMap.put("privileges", privilegeList);

    }

    private void fillProductMap(Map<String, Object> dataMap) {
        Map<String, Object> productMap = new HashMap<>(3);
        productMap.put("logoutUrl", logoutUrl);
        productMap.put("productId", productId);
        productMap.put("productName", StringUtils.isBlank(productName) ? productId : productName);

        dataMap.put("product", productMap);

    }

    private void fillUserMap(Map<String, Object> dataMap, String passport) {

        Map<String, Object> userMap = new HashMap<>(1);
        userMap.put("passport", passport);

        dataMap.put("user", userMap);
    }


}
