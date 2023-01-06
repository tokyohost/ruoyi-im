package com.xim.Store;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.xim.Constants.SocketConstants;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/4 15:31
 * @Content
 */

public class ChannelStore {
    Logger log = LoggerFactory.getLogger(getClass());
    private final ConcurrentHashMap<String, ReentrantReadWriteLock> locks = new ConcurrentHashMap<>();
    private final ConcurrentHashSet<ChannelHandlerContext> allChannel = new ConcurrentHashSet<>();
    private final ConcurrentHashMap<String, HashSet<ChannelHandlerContext>> store = new ConcurrentHashMap<>();

    public Optional<HashSet<ChannelHandlerContext>> findById(String id) {
        if (Objects.isNull(id)) {
            return Optional.empty();
        }
        ReentrantReadWriteLock reentrantReadWriteLock = locks.getOrDefault(id, new ReentrantReadWriteLock());
        locks.put(id, reentrantReadWriteLock);
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        boolean b = readLock.tryLock();
        try {
            if (b) {
                HashSet<ChannelHandlerContext> orDefault = store.getOrDefault(id, null);
                return Objects.isNull(orDefault) ? Optional.empty() : Optional.of(orDefault);
            } else {
                log.error("lock cannot get");
                return Optional.empty();
            }
        } finally {
            readLock.unlock();
        }
    }

    public boolean registChannel(String id, ChannelHandlerContext ctx) {
        ctx.channel().config().setConnectTimeoutMillis(1000 * 60 * 60 * 24);
        ReentrantReadWriteLock readWriteLock = locks.getOrDefault(id, new ReentrantReadWriteLock());
        locks.put(id, readWriteLock);
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            HashSet<ChannelHandlerContext> channelHandlerContexts = new HashSet<>();
            channelHandlerContexts.add(ctx);
            allChannel.add(ctx);
            boolean b = store.containsKey(id);
            if (b) {
                //add
                HashSet<ChannelHandlerContext> ctxs = store.get(id);
                ctxs.addAll(channelHandlerContexts);
                store.put(id, ctxs);
                return true;
            } else {
                //new ChannelSet
                store.put(id, channelHandlerContexts);
                return true;
            }
//                log.error("cannot get lock add channel fail");
//                return false;
        } finally {
            writeLock.unlock();
        }
    }

    public boolean unRegistChannel(ChannelHandlerContext ctx) {
        allChannel.remove(ctx);
        boolean b = ctx.channel().hasAttr(SocketConstants.USER_ID);
        if (b) {
            String id = ctx.channel().attr(SocketConstants.USER_ID).get();
            ReentrantReadWriteLock readWriteLock = locks.getOrDefault(id, new ReentrantReadWriteLock());
            locks.put(id, readWriteLock);
            ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
            writeLock.lock();
            try {
                boolean b1 = store.containsKey(id);
                if (b1) {
                    //remove
                    HashSet<ChannelHandlerContext> ctxs = store.get(id);
                    ctxs.remove(ctx);
                    store.put(id, ctxs);
                    return true;
                } else {
                    return true;
                }
            } finally {
                writeLock.unlock();
            }
        } else {
            log.error("channel loss attr ! unRegistChannel Fail");
            return false;
        }
    }

    public Integer getChannelCount() {
        return allChannel.size();
    }

    public ConcurrentHashMap<String, ReentrantReadWriteLock> getLocks() {
        return locks;
    }
}
