package com.xim.system.utils;

import com.xim.server.ImServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AsyncThreadPoolManage {
    @Autowired
    ImServerProperties imServerProperties;

    AtomicInteger groupChatThreadName = new AtomicInteger(0);
    Executor executorService;
    public Executor getGroupChatExecutor() {
        if (Objects.nonNull(executorService)) {
            return executorService;
        }else{
            synchronized (AsyncThreadPoolManage.class) {
                if (Objects.nonNull(executorService)) {
                    return executorService;
                }else{
                    Executor executorService = new ThreadPoolExecutor(0, imServerProperties.getGroupChatThreadNumber(), 0L, TimeUnit.MILLISECONDS, new SynchronousQueue(), (t)->{
                        Thread thread = Executors.defaultThreadFactory().newThread(t);
                        if (!thread.isDaemon()) {
                            thread.setDaemon(true);
                        }

                        thread.setName("group-chat-" + this.groupChatThreadName.getAndIncrement());
                        return thread;
                    });

                    return executorService;
                }
            }
        }

    }

}
