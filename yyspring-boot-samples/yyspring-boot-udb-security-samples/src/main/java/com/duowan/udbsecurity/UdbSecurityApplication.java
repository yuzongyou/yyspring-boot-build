package com.duowan.udbsecurity;

import com.duowan.yyspring.boot.annotations.YYSpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/10 13:17
 */
@SpringBootApplication
@YYSpringBootApplication(moduleNo = "udbsecure")
public class UdbSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(UdbSecurityApplication.class, args);
    }
}
