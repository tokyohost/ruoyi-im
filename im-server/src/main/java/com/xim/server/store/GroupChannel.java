package com.xim.server.store;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.SubscriberExceptionHandler;

import java.util.concurrent.Executor;

public class GroupChannel extends AsyncEventBus {

    /**
     * 群组唯一id
     */
    private final String groupId;

    public GroupChannel(String identifier, Executor executor,String groupId) {
        super(identifier, executor);
        this.groupId = groupId;
    }

    public GroupChannel(Executor executor, SubscriberExceptionHandler subscriberExceptionHandler,String groupId) {
        super(executor, subscriberExceptionHandler);
        this.groupId = groupId;
    }


    public GroupChannel(Executor executor,String groupId) {
        super(executor);
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }
}
