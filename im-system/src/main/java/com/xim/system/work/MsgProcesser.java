package com.xim.system.work;

import com.xim.system.domain.BaseMsg;
import com.xim.system.enums.MsgType;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/6 21:05
 * @Content
 */

public interface MsgProcesser<T extends BaseMsg<?>> {

    MsgType getMsgType();

    T parseMsg(String msg);

    default void process(T msg, String uid, ChannelHandlerContext ctx){
        process(msg,uid);
    }
    void process(T msg, String uid);
}
