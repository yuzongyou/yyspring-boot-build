package com.duowan.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/7 18:26
 */
@Component
public class LogInfo {

    private static final Logger logger = LoggerFactory.getLogger(LogInfo.class);

    @PostConstruct
    public void init() {

        logger.error("\n\n\n\n\n\n");

        logger.debug("Debug info");
        logger.info("Info info");
        logger.warn("Warn info");
        logger.error("Error info");
    }

}
