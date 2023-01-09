package com.xim.server.store;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public class GroupChannelStore{

    private ConcurrentHashMap<String, GroupEventChannel> groupChannelMap = new ConcurrentHashMap<>();

    public Optional<GroupEventChannel> getGroup(String groupId) {
        if (groupChannelMap.contains(groupId)) {
            return Optional.of(groupChannelMap.get(groupId));
        }else{
            return Optional.empty();
        }
    }

    public boolean setGroup(GroupEventChannel group) {
        if (StringUtils.hasLength(group.getGroupId())) {
            groupChannelMap.put(group.getGroupId(), group);
            return true;
        }else{
            throw new RuntimeException("group id cannot be empty");
        }
    }


}
