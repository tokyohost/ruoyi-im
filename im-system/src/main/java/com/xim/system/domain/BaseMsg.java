package com.xim.system.domain;

import com.alibaba.fastjson2.JSONObject;
import com.xim.system.enums.MsgType;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/6 20:49
 * @Content
 */

public abstract class BaseMsg<T> {


    /**
     * 消息唯一id
     */
    private T msgId;

    /**
     * 消息所属会话id
     * 此会话id 可以是发送人id+接收人id  hash 后结果，
     * 也可以是 接收群的id,群消息没有所属人，所属对象是群
     */
    private T sessionId;

    /**
     * 标识是否是群消息
     */
    private boolean isGroupChatMsg = false;

    /**
     * 消息类型
     */
    private MsgType msgType;

    /**
     * string消息内容
     */
    private String stringMsg;

    /**
     * 发送人
     */
    private T sendBy;

    /**
     * 接收 人/群
     */
    private T sendTo;
    /**
     * 消息原始frame
     */
    private WebSocketFrame webSocketFrame;

    public boolean isGroupChatMsg() {
        return isGroupChatMsg;
    }

    public BaseMsg<T> setGroupChatMsg(boolean groupChatMsg) {
        isGroupChatMsg = groupChatMsg;
        return this;
    }

    public T getMsgId() {
        return msgId;
    }

    public BaseMsg<T> setMsgId(T msgId) {
        this.msgId = msgId;
        return this;
    }

    public T getSessionId() {
        return sessionId;
    }

    public BaseMsg<T> setSessionId(T sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public T getSendBy() {
        return sendBy;
    }

    public BaseMsg<T> setSendBy(T sendBy) {
        this.sendBy = sendBy;
        return this;
    }

    public T getSendTo() {
        return sendTo;
    }

    public BaseMsg<T> setSendTo(T sendTo) {
        this.sendTo = sendTo;
        return this;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public BaseMsg setMsgType(MsgType msgType) {
        this.msgType = msgType;
        return this;
    }

    public String getStringMsg() {
        return stringMsg;
    }

    public BaseMsg setStringMsg(String stringMsg) {
        this.stringMsg = stringMsg;
        return this;
    }

    public WebSocketFrame getWebSocketFrame() {
        return webSocketFrame;
    }

    public BaseMsg setWebSocketFrame(WebSocketFrame webSocketFrame) {
        this.webSocketFrame = webSocketFrame;
        return this;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
