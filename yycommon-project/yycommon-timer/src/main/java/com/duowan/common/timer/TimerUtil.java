package com.duowan.common.timer;

import com.duowan.common.timer.exception.TimerException;
import com.duowan.common.utils.ServerUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时器启动器
 *
 * @author Arvin
 */
public class TimerUtil {

    private TimerUtil() {
        throw new IllegalStateException("Utility class");
    }

    protected static final Logger LOGGER = LoggerFactory.getLogger("TIMERLOG." + TimerUtil.class);

    /**
     * 定时器Timer调度状态， 定时器标识 --> 是否调度
     */
    private static Map<String, Boolean> timerScheduledMap = new HashMap<>();

    /**
     * 定时器任务线程， 定时器标识 --> 是否调度
     */
    private static Map<String, List<Thread>> timerThreadMap = new HashMap<>();


    /**
     * 调度指定的定时器执行
     *
     * @param timer 定时器
     * @return 返回调度任务进行的线程列表
     */
    public static synchronized List<Thread> scheduleTimerExecute(final Timer timer) {

        if (!timer.isEnabled()) {
            LOGGER.info("当前服务器[{}]不启用定时器[{}]。", ServerUtil.getServerIp(), timer.getClass().getSimpleName());
            return new ArrayList<>();
        }

        final String timerId = getTimerId(timer);

        Boolean hadScheduled = timerScheduledMap.get(timerId);
        if (hadScheduled != null && hadScheduled) {
            LOGGER.info("定时器[{}]已被调度过，不进行重新调度！", timerId);
            return timerThreadMap.get(timerId);
        }

        int needAssignedThreadCount = timer.getAssignedThreadCount();
        needAssignedThreadCount = needAssignedThreadCount < 1 ? 1 : needAssignedThreadCount;

        List<Thread> threadList = new ArrayList<>(needAssignedThreadCount);

        timerThreadMap.put(timerId, threadList);
        // 记录调度状态
        timerScheduledMap.put(timerId, true);

        for (int i = 0; i < needAssignedThreadCount; ++i) {

            // 运行定时任务
            final String threadName = timer.getClass().getSimpleName() + "-" + i;

            Thread thread = new Thread(threadName) {
                @Override
                public void run() {
                    executeTimer(timer);
                }
            };

            thread.start();

            threadList.add(thread);
        }

        return threadList;

    }

    private static String getTimerId(Timer timer) {
        String timerId = timer.getId();
        if (StringUtils.isBlank(timerId)) {
            return timer.getClass().getName();
        }
        return timerId;
    }

    private static void executeTimer(Timer timer) {
        try {

            LOGGER.info("定时器[{}]开始调度执行！", getTimerId(timer));

            final Delay delay = timer.getDelay();
            if (delay != null && delay.getDelayMillis() > 0) {
                Thread.sleep(delay.getDelayMillis());
            }

            while (true) {
                // 获取执行周期
                Period period = timer.getPeriod();

                if (period == null) {
                    throw new TimerException("定时器间隔周期没有设置.");
                }

                boolean isContinue = period.sleep();

                doTimerStart(timer);

                if (!isContinue) {
                    break;
                }
            }

        } catch (Exception e) {
            LOGGER.error("定时器[" + timer.getClass() + "]启动失败： " + e.getMessage(), e);
        }
    }

    private static void doTimerStart(Timer timer) {
        try {

            timer.start();

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
