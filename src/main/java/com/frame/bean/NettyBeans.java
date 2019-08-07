package com.frame.bean;

import com.frame.codec.HttpCoustomDecoder;
import com.frame.codec.HttpCoustomEncoder;
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
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.nio.charset.Charset;


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

    @Bean(name = "httpRequestDecoder")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public HttpRequestDecoder getHttpRequestDecoder(){
        return new HttpRequestDecoder();
    }
    @Bean(name = "httpObjectAggregator")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public HttpObjectAggregator getHttpObjectAggregator(){
        //设置解析Http非Get请求的body体最大聚合数
        return new HttpObjectAggregator(65536);
    }
    @Bean(name = "httpCoustomDecoder")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public HttpCoustomDecoder getHttpCoustomDecoder(){
        return new HttpCoustomDecoder(Object.class,true,Charset.forName(config.getCharset()));
    }
    @Bean(name = "httpResponseEncoder")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public HttpResponseEncoder getHttpResponseEncoder(){
        return new HttpResponseEncoder();
    }
    @Bean(name = "httpCoustomEncoder")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public HttpCoustomEncoder getHttpCoustomEncoder(){
        return new HttpCoustomEncoder(true, Charset.forName(config.getCharset()));
    }

    /**
     * 生成workerGroup处理通道Channel，添加pipeline管道组事件
     * @return
     */
    @Bean(name = "httpChannelInitializer")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)         //非单例模式创建Bean   每次使用BeanFactory获取对象都会是一个新的
    public ChannelInitializer<SocketChannel> getHttpChannelInitializer() {

        return new ChannelInitializer<SocketChannel>() {
            @Autowired
            SpringUtils springUtils;
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                //ByteBuf->fullHttpRequest解码器    入站
                pipeline.addLast(springUtils.getBean(HttpRequestDecoder.class));
                //TCP 消息聚合    入站
                pipeline.addLast(springUtils.getBean(HttpObjectAggregator.class));
                //fullHttpRequest->CoustomHttpRequest解码器     入站
                pipeline.addLast(springUtils.getBean(HttpCoustomDecoder.class));
                //fullHttpResponse->ByteBuf编码器    出站
                pipeline.addLast(springUtils.getBean(HttpResponseEncoder.class));
                //CoustomHttpResponse->fullHttpResponse编码器  出站
                pipeline.addLast(springUtils.getBean(HttpCoustomEncoder.class));
                //业务处理
                pipeline.addLast(springUtils.getBean(HttpHandler.class));
            }
        };
    }


}
