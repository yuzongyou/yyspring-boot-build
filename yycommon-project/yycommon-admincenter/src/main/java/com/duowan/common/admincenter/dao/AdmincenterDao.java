package com.duowan.common.admincenter.dao;

import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 9:08
 */
public interface AdmincenterDao {

    /**
     * 获取用户权限
     *
     * @param username 用户名
     * @return 返回用户权限ID列表
     */
    Set<String> getPrivilegeIds(String username);
}
