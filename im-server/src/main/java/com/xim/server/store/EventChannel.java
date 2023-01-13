package com.xim.server.store;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.SubscriberExceptionHandler;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public class EventChannel extends AsyncEventBus {

    AtomicInteger subscribers = new AtomicInteger(0);


    public EventChannel(String identifier, Executor executor) {
        super(identifier, executor);
    }

    public EventChannel(Executor executor, SubscriberExceptionHandler subscriberExceptionHandler) {
        super(executor, subscriberExceptionHandler);
    }


    public EventChannel(Executor executor) {
        super(executor);
    }

    @Override
    public void register(Object object) {
        super.register(object);
        subscribers.getAndIncrement();
    }

    @Override
    public void unregister(Object object) {
        super.unregister(object);
        subscribers.getAndDecrement();
    }

    public AtomicInteger getSubscribersCount() {
        return subscribers;
    }

    public EventChannel setSubscribers(AtomicInteger subscribers) {
        this.subscribers = subscribers;
        return this;
    }
}
