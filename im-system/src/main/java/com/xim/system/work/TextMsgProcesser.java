package com.xim.system.work;

import com.alibaba.fastjson2.JSONObject;
import com.xim.system.domain.TextMsg;
import com.xim.system.enums.MsgType;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/6 21:08
 * @Content
 **/
@Slf4j
@Component
public class TextMsgProcesser implements MsgProcesser<TextMsg>{
    @Override
    public MsgType getMsgType() {
        return MsgType.MSG_TEXT;
    }

    @Override
    public TextMsg parseMsg(String msg) {
        return JSONObject.parseObject(msg,TextMsg.class);
    }

    @Override
    public void process(TextMsg msg, String uid) {
        log.info("text 消息处理:{},{}",uid,msg);
    }
}
