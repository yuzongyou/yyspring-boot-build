package com.duowan.yyspringboot.autoconfigure.ipowner;

import com.duowan.common.ipowner.IpOwnerService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/26 14:21
 */
@Component
public class IpOwnerConsumer implements InitializingBean {

    @Autowired
    private IpOwnerService ipOwnerService;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.err.println("IpOwnerInit: " + ipOwnerService);
    }
}
