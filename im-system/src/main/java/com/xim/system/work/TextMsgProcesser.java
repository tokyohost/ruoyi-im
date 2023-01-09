package com.xim.system.work;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.eventbus.EventBus;
import com.xim.server.store.ChannelStore;
import com.xim.server.store.EventChannel;
import com.xim.server.store.GroupChannelStore;
import com.xim.server.store.GroupEventChannel;
import com.xim.system.domain.TextMsg;
import com.xim.system.enums.MsgType;
import com.xim.server.utils.AsyncThreadPoolManage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
    @Autowired(required = false)
    ChannelStore channelStore;
    @Autowired(required = false)
    GroupChannelStore groupChannelStore;

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
            Optional<GroupEventChannel> group = groupChannelStore.getGroup(msg.getSendTo());
        }else{
            //普通消息
            if (StringUtils.hasLength(msg.getSendTo())) {
                Optional<EventChannel> busById = channelStore.findBusById(msg.getSendTo());
                Optional<EventChannel> busBySendById = channelStore.findBusById(msg.getSendBy());
                //同时发送相同消息给发送者
                busBySendById.ifPresent(bus -> bus.post(msg));
                if (busById.isPresent()) {
//                    广播给该用户下所有存活的链接
                    busById.get().post(msg);
                }else{
                    //该用户没有存活的链接，没有上线
                }

            }

        }
    }
}
