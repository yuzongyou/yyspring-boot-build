package com.duowan.udb.sdk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 * @since 2018/1/16 11:09
 */
public class CK {

    private String name;
    private String value;

    public CK(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public CK() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 将 Cookie 列表转成CK key - value 姓氏
     *
     * @param ckList cookie 列表
     * @return 返回 map
     */
    public static Map<String, String> listToMap(List<CK> ckList) {
        Map<String, String> map = new HashMap<>();

        if (null == ckList || ckList.isEmpty()) {
            return map;
        }

        for (CK ck : ckList) {
            map.put(ck.getName(), ck.getValue());
        }

        return map;
    }

}
