package com.xim.server.work;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.google.common.eventbus.Subscribe;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/9 10:15
 * @Content
 */

public class EventBusChannelHandler<T> implements MsgEncoder<T,String> {

    ChannelHandlerContext context;

    public EventBusChannelHandler(ChannelHandlerContext context) {
        this.context = context;
    }

    @Subscribe
    public void onSendMsg(T msg) {
        String encoder = encoder(msg);
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(encoder);
        context.writeAndFlush(textWebSocketFrame);
    }

    @Override
    public String encoder(T msg) {
        return JSONObject.toJSONString(msg, JSONWriter.Feature.WriteNulls);
    }

    @Override
    public int hashCode() {
        return context.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return context.equals(obj);
    }
}
