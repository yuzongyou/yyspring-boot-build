package com.duowan.recachedns;

import com.duowan.yyspring.boot.annotations.YYSpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/7 11:59
 */
@SpringBootApplication
@YYSpringBootApplication
public class ReCacheDnsApp implements CommandLineRunner {

    public static void main(String[] args) {
        //System.setProperty("PROJECTNO", "yyspring-boot-recachedns-demo");

        SpringApplication.run(ReCacheDnsApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello");

        triggerHostAccess("www.baidu.com");

        System.exit(-1);

    }

    private void triggerHostAccess(String host) {
        try {
            InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
