package com.xim.server.Work;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * websocket 鉴权设置
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/6 19:56
 * @Content
 */
public interface AuthRequestHandler {

    /**
     * 鉴权,
     * @param request
     * @return
     */
    boolean checkAuth(ChannelHandlerContext context,FullHttpRequest request);

    /**
     * 鉴权失败
     * @param context
     * @param request
     * @return
     */
    FullHttpResponse authError(ChannelHandlerContext context,FullHttpRequest request);

    /**
     * 鉴权通过，在此获取用户唯一凭证
     * @param context
     * @param request
     * @return
     */
    String authSucc(ChannelHandlerContext context,FullHttpRequest request);

}

