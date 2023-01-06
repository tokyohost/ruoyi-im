package com.xim.NettyServer;

import com.xim.Constants.SocketConstants;
import com.xim.Store.ChannelStore;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private ChannelStore channelStore;

    public WebSockerServerHandler() {
        super(false);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel Active:"+ctx);
        log.info("active channel count: {}",channelStore.getChannelCount());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel Inactive:"+ctx);
        channelStore.unRegistChannel(ctx);
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        log.info("收到消息："+msg);
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
        log.info("鉴权通过");
        HttpHeaders headers = req.headers();
        String uid = headers.get("uid");
        if (StringUtils.hasLength(uid)) {
            ctx.channel().attr(SocketConstants.USER_ID).set(uid);
            channelStore.registChannel(uid, ctx);
        }else{
            //从uri 取

            Map<String, List<String>> stringListMap = cn.hutool.http.HttpUtil.decodeParams(req.uri(), "UTF-8");
            List<String> strings = stringListMap.get("uid");
            if (!CollectionUtils.isEmpty(strings)) {

                ctx.channel().attr(SocketConstants.USER_ID).set(strings.get(0));
                channelStore.registChannel(strings.get(0), ctx);
            }else {
                log.error("链接未携带uid");
            }
        }
        //往下走
//        super.channelRead(ctx,req);
        ctx.fireChannelRead(req);

    }



    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        // 生成错误页面
        HttpResponseStatus responseStatus = res.status();
        if (responseStatus.code() != 200) {
            ByteBufUtil.writeUtf8(res.content(), responseStatus.toString());
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }
        // 发送response
        boolean keepAlive = HttpUtil.isKeepAlive(req) && responseStatus.code() == 200;
        HttpUtil.setKeepAlive(res, keepAlive);
        ChannelFuture future = ctx.write(res);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
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
