package com.duowan.common.alarm;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executors;

/**
 * @author dw_xiajiqiu1
 */
public class AsyncEventService {

    /** 事件总线 */
    private EventBus bus = new AsyncEventBus(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2));

    private static class Holder {
        static final AsyncEventService INSTANCE = new AsyncEventService();
    }


    public static AsyncEventService getInstance() {
        return Holder.INSTANCE;
    }

    private AsyncEventService() {
    }

    public void register(Object subscriber) {
        bus.register(subscriber);
    }

    public void post(Object event) {
        bus.post(event);
    }
}
