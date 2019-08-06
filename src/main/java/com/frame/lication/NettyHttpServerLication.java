package com.frame.lication;

import com.frame.config.NettyConfig;
import com.frame.utils.SpringUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class NettyHttpServerLication {
    private static final Logger logger = LoggerFactory.getLogger(NettyHttpServerLication.class);
    @Autowired
    NettyConfig config;
    @Autowired
    EventLoopGroup bossGroup;
    @Autowired
    EventLoopGroup workerGroup;
    @Autowired
    SpringUtils springUtils;

    public void run() throws InterruptedException, UnknownHostException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .handler(springUtils.getBean(LoggingHandler.class))
                .childHandler(springUtils.getBean(ChannelInitializer.class));
        ChannelFuture channelFuture = serverBootstrap.bind(config.getPort()).sync();
        channelFuture.channel().closeFuture().sync();
        logger.info("Netty Http Service is Started！");
        logger.info("address:" + InetAddress.getLocalHost().getHostAddress() + ":" + config.getPort());
    }
}
