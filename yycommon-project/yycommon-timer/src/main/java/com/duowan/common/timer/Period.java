package com.duowan.common.timer;

/**
 * 定时器执行周期
 *
 * @author Arvin
 */
public interface Period {

    /**
     * 睡眠到周期到了
     *
     * @return 是否还继续下一次
     */
    boolean sleep();
}
