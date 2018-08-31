package com.duowan.esb.core;

import java.util.List;

/**
 * @author Arvin
 */
public interface EnvReader {

    /**
     * 读取当前运行环境
     *
     * @return 返回当前允许环境，默认是 dev
     */
    String readEnv();

    /**
     * 获取资源文件搜索路径， 如 /data/app/testapp/
     *
     * @return 返回资源路径列表
     */
    List<String> getResourceLookupPaths();
}
