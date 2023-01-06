package com.xim.NettyServer;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.URLUtil;
import com.xim.ImServerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.ResourceLeakDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/3 10:55
 * @Content
 */

public class ServerRuner {

    ImServerProperties imServerProperties;
    Logger log = LoggerFactory.getLogger(getClass());
    WebSockerServerHandler webSockerServerHandler;
    MessageServerHandler messageServerHandler;


    public ServerRuner(ImServerProperties imServerProperties, WebSockerServerHandler webSockerServerHandler, MessageServerHandler messageServerHandler) {
        this.imServerProperties = imServerProperties;
        this.webSockerServerHandler = webSockerServerHandler;
        this.messageServerHandler = messageServerHandler;
    }

    private final NioEventLoopGroup bossGroup = new NioEventLoopGroup(32);
    private final NioEventLoopGroup workerGroup = new NioEventLoopGroup(32);

    public void runServer() throws Exception {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup)

                // 指定Channel
                .channel(NioServerSocketChannel.class)

                //服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
//                .option(ChannelOption.SO_BACKLOG, 1024)

                //设置TCP长连接,一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true)

                //将小的数据包包装成更大的帧进行传送，提高网络的负载
                .childOption(ChannelOption.TCP_NODELAY, true)

                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast("http编码",new HttpServerCodec());
                        ch.pipeline().addLast(new ChunkedWriteHandler());
                        ch.pipeline().addLast(new HttpObjectAggregator(100*1024*1024));
                        //数据压缩
//                        ch.pipeline().addLast(new WebSocketServerCompressionHandler());
                        ch.pipeline().addLast(webSockerServerHandler);
                        ch.pipeline().addLast("websocket 协议处理",new WebSocketServerProtocolHandler(imServerProperties.getPath(),true));
                        ch.pipeline().addLast("message handler", messageServerHandler);

                    }
                });

        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
        ChannelFuture bind = serverBootstrap.bind(imServerProperties.getPort());
        bind.sync().channel();
        log.info("im服务启动成功，监听端口:{},地址:{}",imServerProperties.getPort(),"ws://"+ NetUtil.LOCAL_IP +":"+ imServerProperties.getPort());
    }

    @PreDestroy
    public void destory() throws InterruptedException {
        bossGroup.shutdownGracefully().sync();
        workerGroup.shutdownGracefully().sync();
    }
}
