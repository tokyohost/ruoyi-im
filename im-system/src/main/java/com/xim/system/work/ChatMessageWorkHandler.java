package com.xim.system.work;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson2.JSONObject;
import com.xim.server.work.TextMessageWorkHandler;
import com.xim.system.domain.BaseMsg;
import com.xim.system.enums.MsgType;
import com.xim.system.constant.MsgConstant;
import com.xim.system.utils.MsgProcesserManage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/6 20:28
 * @Content
 */

@SuppressWarnings("unchecked")
@Component
public class ChatMessageWorkHandler implements TextMessageWorkHandler {
    Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    MsgProcesserManage msgProcessers;

    //生成唯一msgid
    Snowflake snowflake = new Snowflake();
    @Override
    public void handMessage(String msg, String uid, ChannelHandlerContext ctx) {
        log.info("ChatMessageWorkHandler 收到用户:"+uid+" 消息:"+msg);

    }

    @Override
    public boolean handJsonMessage(String msg, String uid, ChannelHandlerContext ctx, WebSocketFrame frame) {
        log.info("收到json 消息,uid:{},msg:{}",uid,msg);
        JSONObject parse = JSONObject.parse(msg);
        String msgType = parse.getObject(MsgConstant.MSG_TYPE, String.class);
        if (Objects.isNull(msgType)) {
            throw new RuntimeException("未知json 消息类型");
        }
        boolean processed = false;
        MsgType[] values = MsgType.values();
        for (MsgType value : values) {
            if (msgType.equalsIgnoreCase(value.getType())) {
                MsgProcesser processer = msgProcessers.getProcesser(value);
                BaseMsg parseMsg = processer.parseMsg(msg);

                parseMsg.setMsgId(generateMsgId(msg,uid,ctx));
                parseMsg.setMsgType(value);
                parseMsg.setWebSocketFrame(frame);
                processer.process(parseMsg,uid,ctx);
                processed = true;
                break;
            }
        }

        if (!processed) {
            log.error("未找到此消息类型的消息处理器:{}",msgType);
        }
        return true;
    }

    @Override
    public boolean handTextMessage(String msg, String uid, ChannelHandlerContext ctx) {
        log.info("收到Text 消息,uid:{},msg:{}",uid,msg);
        return true;
    }

    @Override
    public String generateMsgId(String msg, String uid, ChannelHandlerContext ctx) {
        return snowflake.nextIdStr();
    }
}
