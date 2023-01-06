package com.xim.system.enums;

import org.omg.CORBA.UNKNOWN;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/6 20:49
 * @Content
 */
public enum MsgType{
    /**
     * 文本消息
     */

    MSG_TEXT("text"),

    /**
     * 图片消息
     */
    MSG_IMAGE("image"),
    /**
     * 文件消息
     */
    MSG_FILE("file"),
    /**
     * 语音消息
     */
    MSG_VOICE("voice"),

    /**
     * 复杂html消息
     */
    MSG_HTML("html"),
    /**
     * 关闭消息
     */
    CLOSE("close"),
    /**
     * 未知消息
     */
    UNKNOWN("unknown");
    private String type;
    MsgType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public MsgType setType(String type) {
        this.type = type;
        return this;
    }
}
