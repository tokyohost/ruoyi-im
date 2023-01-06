package com.xim.server.Work;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/4 16:55
 * @Content
 */
public interface TextMessageWorkHandler {

    void handMessage(String msg,String uid, ChannelHandlerContext ctx);

    /**
     * 如果消息是json格式的，在此处理后返回true，
     * 则 {@link #handMessage(String, String, ChannelHandlerContext)} 不会再处理,
     * 默认返回false 则继续交由 {@link #handMessage(String, String, ChannelHandlerContext)} 处理
     * @param msg
     * @param uid
     * @param ctx
     * @return
     */
    default boolean handJsonMessage(String msg, String uid, ChannelHandlerContext ctx){
        return false;
    }
    default boolean handJsonMessage(String msg, String uid, ChannelHandlerContext ctx, WebSocketFrame frame){
        return handJsonMessage(msg,uid,ctx);
    }

    /**
     * 如果消息是文本格式的，在此处理后返回true，
     * 则 {@link #handMessage(String, String, ChannelHandlerContext)} 不会再处理,
     * 默认返回false 则继续交由 {@link #handMessage(String, String, ChannelHandlerContext)} 处理
     * @param msg
     * @param uid
     * @param ctx
     * @return
     */
    default boolean handTextMessage(String msg, String uid, ChannelHandlerContext ctx){
        return false;
    }

}
