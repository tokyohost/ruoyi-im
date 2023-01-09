package com.xim.server.nettyServer;

import com.xim.server.constants.SocketConstants;
import com.xim.server.store.ChannelStore;
import com.xim.server.work.AuthRequestHandler;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

import static com.xim.server.utils.HttpUtils.sendHttpResponse;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/3 10:59
 * @Content
 */

@ChannelHandler.Sharable
@Component
public class WebSockerServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    Logger log = LoggerFactory.getLogger(getClass());
    private ChannelStore channelStore;

    @Autowired
    private List<AuthRequestHandler> authRequestHandlers;

    public WebSockerServerHandler(ChannelStore channelStore) {
        super(false);
        this.channelStore = channelStore;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channel Active:"+ctx);
        log.debug("active channel count: {}",channelStore.getChannelCount());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channel Inactive:"+ctx);
        channelStore.unRegistChannel(ctx);
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        handleHttpRequest(ctx, msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * 可以在此进行鉴权操作等
     * @param ctx
     * @param req
     * @throws IOException
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        // 处理异常
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), BAD_REQUEST,
                    ctx.alloc().buffer(0)));
            return;
        }

        // 只允许get请求
        if (!GET.equals(req.method())) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), FORBIDDEN,
                    ctx.alloc().buffer(0)));
            return;
        }

        //鉴权+绑定user
        if(CollectionUtils.isEmpty(authRequestHandlers)){
            throw new RuntimeException("未配置鉴权器");
        }

        for (AuthRequestHandler requestHandler : authRequestHandlers) {
            boolean b = requestHandler.checkAuth(ctx, req);
            if (b) {
                String uid = requestHandler.authSucc(ctx, req);
                ctx.channel().attr(SocketConstants.USER_ID).set(uid);
                channelStore.registChannel(uid, ctx);
            }else{
                FullHttpResponse fullHttpResponse = requestHandler.authError(ctx, req);
                sendHttpResponse(ctx, req, fullHttpResponse);
                return;
            }
        }

        ctx.fireChannelRead(req);

    }





    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 异常处理
        log.error("出现异常",cause);
        ctx.close();
    }

    private static String getWebSocketLocation(FullHttpRequest req) {
        String location =  req.headers().get(HttpHeaderNames.HOST);
        return "ws://" + location;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerAdded");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelRegistered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelUnregistered");
        super.channelUnregistered(ctx);
    }
}
