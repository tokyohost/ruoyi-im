package com.xim.server.store;

import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GroupChannelStore<T extends GroupChannel> {

    private ConcurrentHashMap<String, T> groupChannelMap = new ConcurrentHashMap<>();

    public Optional<T> getGroup(@NotNull String groupId) {
        if (groupChannelMap.contains(groupId)) {
            return Optional.of(groupChannelMap.get(groupId));
        }else{
            return Optional.empty();
        }
    }

    public boolean setGroup(T group) {
        if (StringUtils.hasLength(group.getGroupId())) {
            groupChannelMap.put(group.getGroupId(), group);
            return true;
        }else{
            throw new RuntimeException("group id cannot be empty");
        }
    }


}
