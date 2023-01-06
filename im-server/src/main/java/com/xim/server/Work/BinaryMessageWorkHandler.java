package com.xim.server.Work;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * 二进制消息处理
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/6 20:19
 * @Content
 */

public interface BinaryMessageWorkHandler {


    void handMessage(BinaryWebSocketFrame msg, String uid, ChannelHandlerContext ctx);
}
