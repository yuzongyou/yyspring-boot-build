package com.duowan.udb.security;

/**
 * UDB 验证模式， 本地弱验证，远程强验证
 *
 * @author Arvin
 */
public enum CheckMode {

    /**
     * 本地弱验证
     */
    WEAK,

    /**
     * 远程强验证
     */
    STRONG,

    /**
     * 不需要验证UDB
     */
    NONE,

    /** 默认 */
    DEFAULT
}
