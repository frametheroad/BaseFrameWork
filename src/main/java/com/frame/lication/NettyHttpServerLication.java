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
        //Netty常规套路
        ServerBootstrap serverBootstrap = new ServerBootstrap();
//        NioServerSocketChannel TCP 协议 使用NIO
//        OioServerSocketChannel TCP 协议 使用IO 还有跟多。需要了解自己研究Netty
//        group(接受请求线程组,处理请求线程组)    请求进来会进入bossGroup 直接分发给workerGroup去做处理
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
//                给bossGroup添加日志处理
                .handler(springUtils.getBean(LoggingHandler.class))
//                给workerGroup创建管道Channel
                .childHandler(springUtils.getBean(ChannelInitializer.class));
//        绑定端口
        ChannelFuture channelFuture = serverBootstrap.bind(config.getPort()).sync();
        channelFuture.channel().closeFuture().sync();
        logger.info("Netty Http Service is Started！");
        logger.info("address:" + InetAddress.getLocalHost().getHostAddress() + ":" + config.getPort());
    }
}
