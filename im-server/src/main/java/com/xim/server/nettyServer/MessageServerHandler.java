package com.xim.server.nettyServer;

import cn.hutool.json.JSONUtil;
import com.xim.server.constants.SocketConstants;
import com.xim.server.ImServerProperties;
import com.xim.server.store.ChannelStore;
import com.xim.server.work.TextMessageWorkHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/3 17:19
 * @Content
 */

@Component
@ChannelHandler.Sharable
public class MessageServerHandler extends ChannelInboundHandlerAdapter {

    Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    ChannelStore channelStore;
    @Autowired
    List<TextMessageWorkHandler> textMessageWorkHandlers;

    @Autowired
    ImServerProperties imServerProperties;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            log.error("握手成功");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        } else {
            super.channelRead(ctx, msg);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        // 处理各种websocket的frame信息
        if (frame instanceof CloseWebSocketFrame) {
            ctx.channel().close();
            frame.release();
            return;
        }
        if (frame instanceof PingWebSocketFrame || checkHeartBeat(frame)) {
            ctx.write(new PongWebSocketFrame(frame.content().retain()));
            ctx.writeAndFlush(new TextWebSocketFrame(imServerProperties.getHeartbeatBody()));
            frame.release();
            return;
        }
        if (frame instanceof TextWebSocketFrame) {
            // 直接返回
            log.debug("msg:" + frame.toString());
            String uid = null;
            if (ctx.channel().hasAttr(SocketConstants.USER_ID)) {
                uid = ctx.channel().attr(SocketConstants.USER_ID).get();

            }

            for (TextMessageWorkHandler textMessageWorkHandler : textMessageWorkHandlers) {
                String text = ((TextWebSocketFrame) frame).text();
                boolean typeJSON = JSONUtil.isTypeJSON(text);
                if(typeJSON) {
                    //todo json消息
                    boolean handJsonMessage = textMessageWorkHandler.handJsonMessage(text, uid, ctx, frame);
                    if(!handJsonMessage){
                        textMessageWorkHandler.handMessage(text, uid, ctx);
                    }
                }else {
                    //todo 文本消息
                    boolean handTextMessage = textMessageWorkHandler.handTextMessage(text, uid, ctx);
                    if(!handTextMessage){
                        textMessageWorkHandler.handMessage(text, uid, ctx);
                    }
                }

            }
            frame.release();
            return;
        }
        if (frame instanceof BinaryWebSocketFrame) {
            // 直接返回
            ctx.write(frame.retain());
            frame.release();
        }
    }

    private boolean checkHeartBeat(WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) frame).text();
            if (StringUtils.isEmpty(text)) {
                return false;
            }
            if (text.equals(imServerProperties.getHeartbeatBody())) {
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }


}
