package com.xim.server.utils;

import cn.hutool.system.SystemUtil;
import com.xim.server.ImServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class AsyncThreadPoolManage {


    private static AtomicInteger groupChatThreadName = new AtomicInteger(0);
    private static Executor executorService;
    public static Executor getGroupChatExecutor() {
        if (Objects.nonNull(executorService)) {
            return executorService;
        }else{
            synchronized (AsyncThreadPoolManage.class) {
                if (Objects.nonNull(executorService)) {
                    return executorService;
                }else{
                    executorService = new ThreadPoolExecutor(0, SystemUtil.getTotalThreadCount() * 2, 30L, TimeUnit.MILLISECONDS, new SynchronousQueue(), (t)->{
                        Thread thread = Executors.defaultThreadFactory().newThread(t);
                        if (!thread.isDaemon()) {
                            thread.setDaemon(true);
                        }

                        thread.setName("group-chat-" + groupChatThreadName.getAndIncrement());
                        return thread;
                    });

                    return executorService;
                }
            }
        }

    }

}
