package com.xim.system.work;

import com.alibaba.fastjson2.JSONObject;
import com.xim.server.store.GroupChannel;
import com.xim.server.store.GroupChannelStore;
import com.xim.system.domain.TextMsg;
import com.xim.system.enums.MsgType;
import com.xim.server.utils.AsyncThreadPoolManage;
import lombok.extern.slf4j.Slf4j;
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
