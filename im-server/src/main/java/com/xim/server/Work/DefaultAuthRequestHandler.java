package com.xim.server.Work;

import com.xim.server.ImServerProperties;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/6 20:01
 * @Content
 */

public class DefaultAuthRequestHandler implements AuthRequestHandler {

    ImServerProperties imServerProperties;

    public DefaultAuthRequestHandler(ImServerProperties imServerProperties) {
        this.imServerProperties = imServerProperties;
    }

    Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public boolean checkAuth(ChannelHandlerContext context, FullHttpRequest request) {
        log.info("鉴权通过");
        return true;
    }

    @Override
    public FullHttpResponse authError(ChannelHandlerContext context, FullHttpRequest request) {
        return new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.UNAUTHORIZED);
    }

    @Override
    public String authSucc(ChannelHandlerContext context, FullHttpRequest request) {
        HttpHeaders headers = request.headers();
        String uid = headers.get(imServerProperties.getAuthKey());
        if (StringUtils.hasLength(uid)) {
            return uid;
        }else{
            //从uri 取
            Map<String, List<String>> stringListMap = cn.hutool.http.HttpUtil.decodeParams(request.uri(), "UTF-8");
            List<String> strings = stringListMap.get(imServerProperties.getAuthKey());
            if (!CollectionUtils.isEmpty(strings)) {
                return strings.get(0);
            }else {
                log.error("链接未携带鉴权凭证,链接终止");
                throw new RuntimeException("非法用户");
            }
        }
    }
}
