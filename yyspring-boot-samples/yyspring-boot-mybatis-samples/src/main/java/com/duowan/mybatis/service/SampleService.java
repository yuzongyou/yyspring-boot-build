package com.duowan.mybatis.service;

import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2019/1/9 14:32
 */
public interface SampleService {

    List<String> getDatabases();

    List<String> getTables();

    String selectSelf(String self);
}
