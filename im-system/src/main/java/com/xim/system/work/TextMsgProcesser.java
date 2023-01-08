package com.xim.system.work;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.eventbus.AsyncEventBus;
import com.xim.system.domain.TextMsg;
import com.xim.system.enums.MsgType;
import com.xim.system.utils.AsyncThreadPoolManage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/6 21:08
 * @Content
 **/
@Slf4j
@Component
public class TextMsgProcesser implements MsgProcesser<TextMsg>{
    @Autowired
    AsyncThreadPoolManage asyncThreadPoolManage;
    @Autowired
    GroupChannelStore<GroupChannel> groupChannelStore;

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

        if (msg.isGroupChatMsg()) {
            //群聊消息
            Optional<GroupChannel> group = groupChannelStore.getGroup(msg.getSendTo());
        }else{
            //普通消息

        }
    }
}
