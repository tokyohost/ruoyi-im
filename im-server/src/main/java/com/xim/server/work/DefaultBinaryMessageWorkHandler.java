package com.xim.server.work;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/6 20:20
 * @Content
 */

public class DefaultBinaryMessageWorkHandler implements BinaryMessageWorkHandler {
    Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public void handMessage(BinaryWebSocketFrame msg, String uid, ChannelHandlerContext ctx) {
        log.info("DefaultBinaryMessageWorkHandler 收到用户:{} 消息:{}",uid, msg);
    }

}
