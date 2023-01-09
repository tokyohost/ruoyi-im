package com.xim.server.store;

import com.google.common.eventbus.SubscriberExceptionHandler;

import java.util.concurrent.Executor;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/9 11:02
 * @Content
 */

public class GroupEventChannel extends EventChannel{
    /**
     * 群组唯一id
     */
    private final String groupId;

    public GroupEventChannel(String identifier, Executor executor, String groupId) {
        super(identifier, executor);
        this.groupId = groupId;
    }

    public GroupEventChannel(Executor executor, SubscriberExceptionHandler subscriberExceptionHandler, String groupId) {
        super(executor, subscriberExceptionHandler);
        this.groupId = groupId;
    }

    public GroupEventChannel(Executor executor, String groupId) {
        super(executor);
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }
}
