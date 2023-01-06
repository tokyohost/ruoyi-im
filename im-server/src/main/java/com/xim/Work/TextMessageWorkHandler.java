package com.xim.Work;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/4 16:55
 * @Content
 */
public interface TextMessageWorkHandler {

    void handMessage(String msg,String uid, ChannelHandlerContext ctx);

}
