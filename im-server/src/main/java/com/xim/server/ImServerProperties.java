package com.xim.server;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/5 17:40
 * @Content
 */
@ConfigurationProperties(prefix = "im.server")
public class ImServerProperties {

    /**
     * websocket 监听路径
     */
    String path;

    /**
     * 监听端口
     */
    Integer port;

    /**
     * 鉴权凭证存储的key，鉴权凭证默认会从header和uri中获取
     */
    String authKey;

    /**
     * 群聊消息分发线程
     */
    Integer groupChatThreadNumber;

    public Integer getGroupChatThreadNumber() {
        return groupChatThreadNumber;
    }

    public ImServerProperties setGroupChatThreadNumber(Integer groupChatThreadNumber) {
        this.groupChatThreadNumber = groupChatThreadNumber;
        return this;
    }

    /**
     * 心跳包内容
     */
    String heartbeatBody;

    /**
     * 心跳包响应
     */
    String heartBeatReply;

    public String getAuthKey() {
        return authKey;
    }

    public ImServerProperties setAuthKey(String authKey) {
        this.authKey = authKey;
        return this;
    }

    public String getPath() {
        return path;
    }

    public ImServerProperties setPath(String path) {
        this.path = path;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public ImServerProperties setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getHeartbeatBody() {
        return heartbeatBody;
    }

    public ImServerProperties setHeartbeatBody(String heartbeatBody) {
        this.heartbeatBody = heartbeatBody;
        return this;
    }

    public String getHeartBeatReply() {
        return heartBeatReply;
    }

    public ImServerProperties setHeartBeatReply(String heartBeatReply) {
        this.heartBeatReply = heartBeatReply;
        return this;
    }
}
