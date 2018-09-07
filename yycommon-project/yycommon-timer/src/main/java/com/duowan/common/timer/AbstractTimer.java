package com.duowan.common.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Arvin
 */
public abstract class AbstractTimer implements Timer {

    protected final Logger logger = LoggerFactory.getLogger("TIMERLOG." + this.getClass());

    @Override
    public String getId() {
        return this.getClass().getName();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int getAssignedThreadCount() {
        return 1;
    }

    @Override
    public Delay getDelay() {
        return null;
    }
}
