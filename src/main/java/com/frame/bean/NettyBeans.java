package com.frame.bean;

import com.frame.codec.HttpCoustomDecoder;
import com.frame.config.NettyConfig;
import com.frame.handler.HttpHandler;
import com.frame.utils.SpringUtils;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
public class NettyBeans {
    private static final Logger logger = LoggerFactory.getLogger(NettyBeans.class);
    @Autowired
    private NettyConfig config;

    @Bean(name = "bossGroup")
    public EventLoopGroup getBossGroup() {
        return new NioEventLoopGroup(config.getBossThread());
    }

    @Bean(name = "workerGroup")
    public EventLoopGroup getWorkerGroup() {
        return new NioEventLoopGroup(config.getWorkerThread());
    }

    @Bean(name = "loggingHandler")
    //创建非单例类
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public LoggingHandler getLoggingHandler() {
        return new LoggingHandler(LogLevel.INFO);
    }

    @Bean(name = "httpServerCodec")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public HttpServerCodec getHttpServerCodec() {
        return new HttpServerCodec();
    }

    @Bean(name = "httpChannelInitializer")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ChannelInitializer<SocketChannel> getHttpChannelInitializer() {

        return new ChannelInitializer<SocketChannel>() {
            @Autowired
            SpringUtils springUtils;
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new HttpRequestDecoder());
                pipeline.addLast(new HttpObjectAggregator(65536));
                pipeline.addLast(new HttpCoustomDecoder(Object.class,true));
                pipeline.addLast(new HttpResponseEncoder());
//                pipeline.addLast(springUtils.getBean("httpServerCodec",HttpServerCodec.class));
                pipeline.addLast(springUtils.getBean("httpHandler", HttpHandler.class));
            }
        };
    }


}
