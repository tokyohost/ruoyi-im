package com.xim;

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
}
