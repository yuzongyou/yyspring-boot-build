package com.duowan.udb.sdk;

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
}
