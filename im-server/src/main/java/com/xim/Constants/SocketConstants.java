package com.xim.Constants;

import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.AttributeKey;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/3 18:01
 * @Content
 */

public class SocketConstants {
    public static final String HEART_BEAT = "heartbeat";
    public static final String HEART_BEAT_PONG = "pong";
    public static AttributeKey<String> USER_ID = AttributeKey.valueOf("USER_ID");
}
