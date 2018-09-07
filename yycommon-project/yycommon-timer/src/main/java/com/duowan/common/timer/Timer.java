package com.duowan.common.timer;

/**
 * 定时器接口定义
 *
 * @author Arvin
 */
public interface Timer {

    /**
     * 返回定时器标识
     *
     * @return 定时器标识字符串
     */
    String getId();

    /**
     * 是否启用定时器，通常我们会根据不同环境[开发，测试，生产]来决定是否启动这个定时器
     *
     * @return true - 启用， false - 不启用
     */
    boolean isEnabled();

    /**
     * 获取需要分配的线程数量， 开发者可以定义这个定时器需要多少个线程来执行同一个任务（多个线程并行执行）
     *
     * @return 返回分配的线程数
     */
    int getAssignedThreadCount();

    /**
     * 获取定时器执行周期，即完成一次任务后，等待多长时间执行下一个任务
     *
     * @return 返回执行周期
     */
    Period getPeriod();

    /**
     * 获取任务延迟执行的毫秒数。即应用启动的时候，需要多少时间之后才开始调度执行定时器
     *
     * @return 返回延迟对象，返回 null 表示不延迟
     */
    Delay getDelay();

    /**
     * 执行任务， 将具体的任务实现写在这里
     */
    void start();

}
