package com.duowan.common.thrift.server;

import com.duowan.common.thrift.server.exporter.ThriftServiceWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/26 9:33
 */
public interface ThriftServiceSearcher {

    /**
     * 搜索 Thrift 服务实例对象列表
     *
     * @param acx         Spring 上下文
     * @param environment 当前运行环境
     * @return 返回 Thrift 服务对象实例列表
     */
    List<ThriftServiceWrapper> searchThriftServices(ApplicationContext acx, Environment environment);
}
