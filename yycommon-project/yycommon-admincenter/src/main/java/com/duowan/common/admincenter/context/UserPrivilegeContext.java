package com.duowan.common.admincenter.context;

import com.duowan.common.admincenter.model.Privilege;
import com.duowan.common.admincenter.util.PrivilegeUtil;

import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 9:59
 */
public class UserPrivilegeContext {

    /**
     * 用户权限列表
     **/
    private List<Privilege> privileges;

    private Map<String, Privilege> ipPrivilegeMap;

    /**
     * 用户通行证
     **/
    private String username;

    /**
     * 登录时间
     **/
    private long loginTime;

    public UserPrivilegeContext() {
        this.loginTime = System.currentTimeMillis();
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
        this.ipPrivilegeMap = PrivilegeUtil.extractToIdPrivilegeMap(privileges);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public boolean hasPrivilege(Privilege privilege) {
        if (null == privilege) {
            return true;
        }
        return ipPrivilegeMap.containsKey(privilege.getPrivilegeId());
    }
}
