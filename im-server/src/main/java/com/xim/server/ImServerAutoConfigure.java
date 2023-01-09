package com.xim.server;

import com.xim.server.nettyServer.MessageServerHandler;
import com.xim.server.nettyServer.ServerRuner;
import com.xim.server.nettyServer.WebSockerServerHandler;
import com.xim.server.store.ChannelStore;
import com.xim.server.store.GroupChannelStore;
import com.xim.server.utils.AsyncThreadPoolManage;
import com.xim.server.work.AuthRequestHandler;
import com.xim.server.work.DefaultAuthRequestHandler;
import com.xim.server.work.DefaultTextMessageWorkHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

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

    WebSockerServerHandler webSockerServerHandler;
    MessageServerHandler messageServerHandler;

    ImServerProperties imServerProperties;

    ChannelStore channelStore;
    GroupChannelStore groupChannelStore;

    DefaultTextMessageWorkHandler defaultTextMessageWorkHandler;

    public ImServerAutoConfigure(ImServerProperties imServerProperties) {
        this.imServerProperties = imServerProperties;
    }

    @Bean
    @Order(1)
    public ChannelStore getChannelStore() {
        this.channelStore = new ChannelStore();
        return this.channelStore;
    }
    @Bean
    public GroupChannelStore getGroupChannelStore() {
        this.groupChannelStore = new GroupChannelStore();
        return this.groupChannelStore;
    }

    @Bean
    public WebSockerServerHandler getWebSockerServerHandler() {
        this.webSockerServerHandler = new WebSockerServerHandler(channelStore);
        return this.webSockerServerHandler;
    }

    @Bean
    public MessageServerHandler getMessageServerHandler() {
        this.messageServerHandler = new MessageServerHandler(channelStore);
        return this.messageServerHandler;
    }



    @Bean
    public DefaultTextMessageWorkHandler getDefaultTextMessageWorkHandler() {
        this.defaultTextMessageWorkHandler = new DefaultTextMessageWorkHandler();
        return this.defaultTextMessageWorkHandler;
    }
    @Bean
    public AuthRequestHandler getAuthRequestHandler() {
        return new DefaultAuthRequestHandler(imServerProperties);
    }

    @Bean
    public ServerRuner serverRunner() throws Exception {
        ServerRuner serverRuner = new ServerRuner(imServerProperties, webSockerServerHandler, messageServerHandler);
        serverRuner.runServer();
        return serverRuner;
    }




}
