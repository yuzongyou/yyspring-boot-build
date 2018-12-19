package com.duowan.common.admincenter.context;

import com.duowan.common.admincenter.model.Privilege;
import com.duowan.common.admincenter.util.PrivilegeUtil;
import com.duowan.common.exception.CodeException;
import com.duowan.common.utils.CommonUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 9:18
 */
public class PrivilegeContext {

    /**
     * 顶级权限
     **/
    private List<Privilege> topPrivileges;

    /**
     * 所有的权限对象
     **/
    private List<Privilege> allPrivileges;

    /**
     * URI to 权限对象
     **/
    private Map<String, Privilege> urlPrivilegeMap;

    /**
     * ID to 权限对象
     **/
    private Map<String, Privilege> idPrivilegeMap;

    /**
     * 是否需要检查权限，主要是给开发环境用
     **/
    private boolean needCheckPrivilege = true;

    /**
     * 部包含权限的URI集合
     **/
    private Set<String> noPrivilegeUrls = new HashSet<>();

    public PrivilegeContext(String privilegeXml, boolean needCheckPrivilege) {
        if (StringUtils.isBlank(privilegeXml)) {
            throw new CodeException(500, "权限文件内容不能为空！");
        }

        this.topPrivileges = PrivilegeUtil.parseByXmlRefParent(privilegeXml);

        this.allPrivileges = PrivilegeUtil.extractToList(this.topPrivileges);

        this.urlPrivilegeMap = PrivilegeUtil.extractToUrlPrivilegeMap(this.allPrivileges);

        this.idPrivilegeMap = PrivilegeUtil.extractToIdPrivilegeMap(this.allPrivileges);

        this.needCheckPrivilege = needCheckPrivilege;
    }

    /**
     * 提取当前请求的权限
     *
     * @param request 当前请求对象
     * @return 返回权限对象，如果存在的话
     */
    public Privilege detectPrivilege(HttpServletRequest request) {
        String uri = request.getRequestURI();

        if (this.noPrivilegeUrls.contains(uri)) {
            return null;
        }

        Privilege privilege = this.urlPrivilegeMap.get(uri);
        if (null != privilege) {
            return privilege;
        }

        for (Map.Entry<String, Privilege> entry : this.urlPrivilegeMap.entrySet()) {
            String urlPattern = entry.getKey();
            if (urlPattern.contains(CommonUtil.WILDCARD_START) && CommonUtil.isStartWildcardMatch(uri, urlPattern)) {
                this.urlPrivilegeMap.put(uri, entry.getValue());
                return entry.getValue();
            }
        }

        this.noPrivilegeUrls.add(uri);
        return null;
    }

    public Privilege getPrivilegeById(String privilegeId) {
        return this.idPrivilegeMap.get(privilegeId);
    }

    public List<Privilege> getPrivilegesByIds(Set<String> privilegeIds) {
        List<Privilege> privileges = new ArrayList<>();

        for (Privilege privilege : allPrivileges) {
            if (privilegeIds.contains(privilege.getPrivilegeId())) {
                privileges.add(privilege);
            }
        }

        return privileges;
    }

    public List<Privilege> getAllPrivileges() {
        return allPrivileges;
    }

    public boolean needCheckPrivilege() {
        return needCheckPrivilege;
    }
}
