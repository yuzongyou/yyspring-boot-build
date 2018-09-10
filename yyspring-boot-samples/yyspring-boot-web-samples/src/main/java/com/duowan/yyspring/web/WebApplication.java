package com.duowan.yyspring.web;

import com.duowan.yyspring.boot.annotations.YYSpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/10 8:59
 */
@SpringBootApplication
@YYSpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
