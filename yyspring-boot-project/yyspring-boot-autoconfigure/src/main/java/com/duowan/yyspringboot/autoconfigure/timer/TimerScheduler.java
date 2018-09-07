package com.duowan.yyspringboot.autoconfigure.timer;

import com.duowan.common.timer.Timer;
import com.duowan.common.timer.TimerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 定时器启动器
 *
 * @author Arvin
 */
public class TimerScheduler {

    protected static final Logger logger = LoggerFactory.getLogger("TIMERLOG." + TimerScheduler.class);

    /**
     * 是否已经调度执行了，防止不小心多次运行start方法
     */
    private static boolean hasScheduled = false;

    /**
     * 定时器列表
     */
    @Autowired(required = false)
    private List<Timer> timerList;

    /**
     * 调度所有定时器运行
     */
    @PostConstruct
    public synchronized void start() {

        if (!hasScheduled) {

            // 标记为已经调度
            hasScheduled = true;

            if (timerList != null && !timerList.isEmpty()) {

                for (Timer timer : timerList) {

                    TimerUtil.scheduleTimerExecute(timer);

                }

            }
        }
    }

}
