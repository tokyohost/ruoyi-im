package com.xim.NettyServer;

import com.xim.Constants.SocketConstants;
import com.xim.Store.ChannelStore;
import com.xim.Work.TextMessageWorkHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        if (frame instanceof PingWebSocketFrame) {
            ctx.write(new PongWebSocketFrame(frame.content().retain()));
            frame.release();
            return;
        }
        if (frame instanceof TextWebSocketFrame) {
            // 直接返回
            log.info("msg:" + frame.toString());
            String uid = null;
            if (ctx.channel().hasAttr(SocketConstants.USER_ID)) {
                uid = ctx.channel().attr(SocketConstants.USER_ID).get();

            }

            for (TextMessageWorkHandler textMessageWorkHandler : textMessageWorkHandlers) {
                textMessageWorkHandler.handMessage(((TextWebSocketFrame) frame).text(), uid, ctx);
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


}
