package com.duowan.esb.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Arvin
 */
@SpringBootApplication
public class AbstractMvcStarterTest extends AbstractMvcStarter {

    public static void main(String[] args) throws Exception {
        startApp(AbstractMvcStarterTest.class, args);
    }
}
