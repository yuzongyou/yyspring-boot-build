package com.duowan.common.admincenter.model;

/**
 * @author Arvin
 */
public class PrivilegeItem {

    private String authKeyNav;
    private String idNav;
    private String nameNav;

    private String extAuthKeyNav;
    private String extIdNav;
    private String extNameNav;

    public PrivilegeItem(String extNameNav, String extIdNav, String extAuthKeyNav) {
        this.extNameNav = extNameNav;
        this.extIdNav = extIdNav;
        this.extAuthKeyNav = extAuthKeyNav;

        this.nameNav = extNameNav.trim();
        this.idNav = extIdNav.trim();
        this.authKeyNav = extAuthKeyNav.trim();
    }

    public String getAuthKeyNav() {
        return authKeyNav;
    }

    public void setAuthKeyNav(String authKeyNav) {
        this.authKeyNav = authKeyNav;
    }

    public String getIdNav() {
        return idNav;
    }

    public void setIdNav(String idNav) {
        this.idNav = idNav;
    }

    public String getNameNav() {
        return nameNav;
    }

    public void setNameNav(String nameNav) {
        this.nameNav = nameNav;
    }

    public String getExtAuthKeyNav() {
        return extAuthKeyNav;
    }

    public void setExtAuthKeyNav(String extAuthKeyNav) {
        this.extAuthKeyNav = extAuthKeyNav;
    }

    public String getExtIdNav() {
        return extIdNav;
    }

    public void setExtIdNav(String extIdNav) {
        this.extIdNav = extIdNav;
    }

    public String getExtNameNav() {
        return extNameNav;
    }

    public void setExtNameNav(String extNameNav) {
        this.extNameNav = extNameNav;
    }
}
