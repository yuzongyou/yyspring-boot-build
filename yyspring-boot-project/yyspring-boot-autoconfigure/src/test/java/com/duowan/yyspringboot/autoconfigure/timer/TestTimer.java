package com.duowan.yyspringboot.autoconfigure.timer;

import com.duowan.common.timer.AbstractTimer;
import com.duowan.common.timer.OncePeriod;
import com.duowan.common.timer.Period;
import org.springframework.stereotype.Component;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/7 12:57
 */
@Component
public class TestTimer extends AbstractTimer {

    public static volatile boolean hadRunTestTimer = false;

    @Override
    public Period getPeriod() {
        return new OncePeriod();
    }

    @Override
    public void start() {
        hadRunTestTimer = true;
    }
}
