package com.xim;

import com.xim.NettyServer.MessageServerHandler;
import com.xim.NettyServer.ServerRuner;
import com.xim.NettyServer.WebSockerServerHandler;
import com.xim.Store.ChannelStore;
import com.xim.Work.DefaultTextMessageWorkHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/5 17:40
 * @Content
 */

@Configuration
@ConditionalOnClass(ImServerProperties.class)
@EnableConfigurationProperties(ImServerProperties.class)
@ConditionalOnProperty(value = "im.server.enable",havingValue = "true")
public class ImServerAutoConfigure {

    @Autowired
    WebSockerServerHandler webSockerServerHandler;
    @Autowired
    MessageServerHandler messageServerHandler;

    ImServerProperties imServerProperties;

    @Autowired
    ChannelStore channelStore;
    @Autowired
    DefaultTextMessageWorkHandler defaultTextMessageWorkHandler;

    public ImServerAutoConfigure(ImServerProperties imServerProperties) {
        this.imServerProperties = imServerProperties;
    }

    @Bean
    public WebSockerServerHandler getWebSockerServerHandler() {
        return new WebSockerServerHandler();
    }

    @Bean
    public MessageServerHandler getMessageServerHandler() {
        return new MessageServerHandler();
    }

    @Bean
    public ChannelStore getChannelStore() {
        return new ChannelStore();
    }

    @Bean
    public DefaultTextMessageWorkHandler getDefaultTextMessageWorkHandler() {
        return new DefaultTextMessageWorkHandler(channelStore);
    }

    @Bean
    public ServerRuner startServer() throws Exception {

        ServerRuner serverRuner = new ServerRuner(imServerProperties, webSockerServerHandler, messageServerHandler);
        serverRuner.runServer();
        return serverRuner;
    }



}
