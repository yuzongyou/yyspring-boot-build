package com.duowan.common.admincenter.dao.http;

import com.duowan.common.admincenter.dao.AdmincenterDao;
import com.duowan.common.admincenter.model.CenterInfoType;
import com.duowan.common.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 9:08
 */
public class AdmincenterDaoHttpImpl implements AdmincenterDao {

    public static final String DEFAULT_FETCH_PRIVILEGE_URL = "http://admincenter.game.yy.com/sdk/getCenterInfo.do";

    private static final Logger logger = LoggerFactory.getLogger(AdmincenterDaoHttpImpl.class);

    private String productId;
    private String productKey;

    private String fetchPrivilegeUrl = DEFAULT_FETCH_PRIVILEGE_URL;

    public AdmincenterDaoHttpImpl(String productId, String productKey, String fetchPrivilegeUrl) {

        AssertUtil.assertNotBlank(productId, "Admincenter's productId should not be null");
        AssertUtil.assertNotBlank(productKey, "Admincenter's productKey should not be null");

        this.productId = productId;
        this.productKey = productKey;
        if (StringUtils.isNotBlank(fetchPrivilegeUrl)) {
            this.fetchPrivilegeUrl = fetchPrivilegeUrl;
        }
    }

    @Override
    public Set<String> getPrivilegeIds(String username) {

        StringBuilder url = new StringBuilder(fetchPrivilegeUrl);
        url.append("?productId=").append(productId);
        url.append("&userName=").append(username);
        url.append("&type=").append(CenterInfoType.PRIVILEGE.getId());
        url.append("&key=").append(EncryptUtil.toMd5UpperCase(productId + username + productKey));

        String responseText = HttpUtil.doGet(url.toString());

        return resposneToIdSet(responseText);
    }

    /**
     * 将接口返回信息转换为Set
     *
     * @param resposneText - 接口返回信息,格式:{"result":true,"message":"111,112,113,114,115"}
     * @return - 转换后的Set
     */
    private Set<String> resposneToIdSet(String resposneText) {
        if (StringUtils.isBlank(resposneText)) {
            return new HashSet<>(0);
        }

        Map<String, Object> respMap = JsonUtil.toObjectMap(resposneText);
        boolean success = JsonUtil.getBoolean(respMap, "result", false);

        if (!success) {
            String message = ConvertUtil.toString(respMap.get("message"), "请查看是否在管理中心给此用户分配了对应产品的角色");
            logger.warn("无法获取用户权限： {}", message);
            return new HashSet<>(0);
        }
        String message = ConvertUtil.toString(respMap.get("message"), "");
        if (StringUtils.isBlank(message)) {
            return new HashSet<>(0);
        }
        String[] functionPoints = message.split(",");
        Set<String> set = new HashSet<>(functionPoints.length);
        set.addAll(Arrays.asList(functionPoints));
        return set;
    }
}
