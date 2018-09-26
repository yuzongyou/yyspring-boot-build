package com.duowan.common.thrift.server;

import com.duowan.common.thrift.server.exporter.ThriftServiceWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.*;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/26 9:36
 */
public class BeanNamesThriftServiceSearcher implements ThriftServiceSearcher {

    private final Set<String> beanNames;

    public BeanNamesThriftServiceSearcher(Collection<String> beanNames) {
        this.beanNames = beanNames == null ? new HashSet<>() : new HashSet<>(beanNames);
    }

    @Override
    public List<ThriftServiceWrapper> searchThriftServices(ApplicationContext acx, Environment environment) {
        List<ThriftServiceWrapper> instanceList = new ArrayList<>(beanNames.size());

        for (String beanName : beanNames) {
            instanceList.add(new ThriftServiceWrapper(acx.getBean(beanName)));
        }

        return instanceList;
    }
}
