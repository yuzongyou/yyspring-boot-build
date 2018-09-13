package com.duowan.common.admincenter.model;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 权限对象
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/8/27 11:42
 */
public class Privilege {

    /**
     * 权限ID，数字
     */
    private String privilegeId;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 是否展示菜单
     */
    private boolean showMenu;

    /**
     * css 样式，如果没有的话就默认是 fa fa-circle-o
     */
    private String cssClass;

    /**
     * 父级权限
     */
    private Privilege parent;

    /**
     * 子级权限
     */
    private List<Privilege> children;

    /**
     * URL 地址列表
     */
    private List<String> urlList;

    /**
     * 扩展信息字段
     **/
    private Map<String, String> ext;

    public Privilege(String privilegeId, String name, boolean showMenu, String cssClass, Privilege parent) {
        this.privilegeId = privilegeId;
        this.name = name;
        this.showMenu = showMenu;
        this.cssClass = cssClass;
        this.parent = parent;
    }

    public String getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(String privilegeId) {
        this.privilegeId = privilegeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public Privilege getParent() {
        return parent;
    }

    public void setParent(Privilege parent) {
        this.parent = parent;
    }

    public List<Privilege> getChildren() {
        return children;
    }

    public void setChildren(List<Privilege> children) {
        this.children = children;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public Map<String, String> getExt() {
        return ext;
    }

    public void setExt(Map<String, String> ext) {
        this.ext = ext;
    }

    public String getFirstUrl() {
        return null == urlList || urlList.isEmpty() || StringUtils.isBlank(urlList.get(0)) ? null : urlList.get(0);
    }

    @Override
    public String toString() {
        return "Privilege{" +
                "privilegeId='" + privilegeId + '\'' +
                ", name='" + name + '\'' +
                ", showMenu=" + showMenu +
                ", cssClass='" + cssClass + '\'' +
                ", urlList=" + urlList +
                ", ext=" + ext +
                '}';
    }
}
