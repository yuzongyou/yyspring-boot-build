package com.duowan.ribbon;

import com.netflix.client.ClientFactory;
import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import org.junit.Test;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 16:55
 */
public class RibbonTest {


    @Test
    public void test() throws InterruptedException {
        ConfigurationManager.getConfigInstance()
                .setProperty("thrift.ribbon.listOfServers", "localhost:8080,localhost:25000");

        ILoadBalancer loadBalancer = ClientFactory.getNamedLoadBalancer("thrift");

        IRule chooseRule = new RoundRobinRule();

        chooseRule.setLoadBalancer(loadBalancer);

        while (true) {

            Server server = chooseRule.choose(null);
            System.out.println("request" + server.getHostPort());

            Thread.sleep(1000);

        }

    }
}
