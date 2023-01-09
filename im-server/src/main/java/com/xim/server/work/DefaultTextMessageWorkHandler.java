package com.xim.server.work;

import cn.hutool.core.lang.UUID;
import com.xim.server.store.ChannelStore;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/4 16:57
 * @Content
 */
public class DefaultTextMessageWorkHandler implements TextMessageWorkHandler {
    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void handMessage(String msg, String uid, ChannelHandlerContext ctx) {
//        log.info("收到用户：{} 的消息:{}", uid, msg);
//        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(msg + " cp");
//        ctx.channel().writeAndFlush(textWebSocketFrame);


//        Optional<HashSet<ChannelHandlerContext>> contexts = channelStore.findById(uid);
//        if (contexts.isPresent()) {
//            ConcurrentHashMap<String, ReentrantReadWriteLock> locks = channelStore.getLocks();
//            ReentrantReadWriteLock readWriteLock = locks.getOrDefault(uid, new ReentrantReadWriteLock());
//            ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
//            readLock.lock();
//            try{
//                HashSet<ChannelHandlerContext> channelHandlerContexts = contexts.get();
//                for (ChannelHandlerContext channelHandlerContext : channelHandlerContexts) {
//                    TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(msg + " cp");
//                    channelHandlerContext.channel().writeAndFlush(textWebSocketFrame.retain());
//                }
//            }finally {
//                readLock.unlock();
//            }
//
//        } else {
//            log.warn("user:{} no channel active! send fail!", uid);
//        }

    }
    @Override
    public String generateMsgId(String msg, String uid, ChannelHandlerContext ctx) {
        return UUID.fastUUID().toString();
    }
}
