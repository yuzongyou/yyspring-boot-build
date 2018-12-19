package com.duowan.common.admincenter.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取管理中心信息类型
 *
 * @author Arvin
 */
public enum CenterInfoType {

    /**
     * 获取用户的访问权限
     **/
    PRIVILEGE(1, "访问权限"),
    /**
     * 获取用户的游戏权限
     **/
    GAME(2, "游戏权限"),
    /**
     * 获取用户的产品角色
     **/
    ROLE(3, "角色信息"),
    /**
     * 获取产品的用户信息
     **/
    USERS(4, "用户信息"),
    /**
     * 获取用户的角色信息
     **/
    ROLE_USER(5, "角色用户"),
    /**
     * 获取用户的业务信息
     **/
    SUBJECT(6, "业务信息"),
    /**
     * 获取用户所属用户组
     **/
    TEAM(7, "用户组信息");

    private int id;
    private String name;

    private static final Map<Integer, CenterInfoType> TYPE_MAP = new HashMap<>();

    static {
        for (CenterInfoType type : values()) {
            TYPE_MAP.put(type.id, type);
        }
    }

    CenterInfoType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static CenterInfoType getType(int id) {
        return TYPE_MAP.get(id);
    }
}
