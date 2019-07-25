package com.frame.handler;

import com.alibaba.fastjson.JSON;
import com.frame.annotation.RequestMapping;
import com.frame.annotation.RequestMappingInfo;
import com.frame.config.NettyConfig;
import com.frame.utils.SpringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.frame.handler.bean.HttpLate;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HttpHandler extends SimpleChannelInboundHandler<HttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(HttpHandler.class);
    @Autowired
    NettyConfig config;
    @Autowired
    SpringUtils springUtils;
    @Autowired
    //打算实现Spring多种路径,后期做优化
    Map<String, RequestMappingInfo> routes;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest request) throws Exception {
        FullHttpResponse response=null;
        HttpVersion httpVersion = HttpVersion.HTTP_1_1;
        Charset charset = CharsetUtil.UTF_8;
        HttpResponseStatus responseStatus = HttpResponseStatus.OK;
        ByteBuf content=null;
        String uri = request.uri();
        HttpLate late = new HttpLate(uri,request.method().name());
        if(!uri.startsWith(config.getContentPath())){
            responseStatus = HttpResponseStatus.NOT_FOUND;
            content = Unpooled.copiedBuffer(responseStatus.toString(),charset);
        }else{
            uri = uri.substring(config.getContentPath().length());
            logger.info("This requeest  uri:[ {} ] method:[ {} ]",uri,late.getMethod());
            RequestMappingInfo rmi = routes.get(uri);
            if(null==rmi){
                responseStatus = HttpResponseStatus.NOT_FOUND;
                content = Unpooled.copiedBuffer(responseStatus.toString(),charset);
            }else {
                Object obj = springUtils.getBean(rmi.getBeanName());
                Object restObj = obj.getClass().getDeclaredMethod(rmi.getExecMethod().getName()).invoke(obj);

                content = Unpooled.copiedBuffer(restObj.toString(), CharsetUtil.UTF_8);
            }
        }
        response = new DefaultFullHttpResponse(httpVersion, responseStatus,content);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
        ctx.writeAndFlush(response);
    }
}
