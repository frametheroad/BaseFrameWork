package com.frame.handler;

import com.frame.annotation.RequestMappingInfo;
import com.frame.annotation.RequestMethod;
import com.frame.bean.RouteBeans;
import com.frame.codec.CoustomHttpRequest;
import com.frame.config.NettyConfig;
import com.frame.handler.bean.HttpLate;
import com.frame.utils.SpringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
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

import java.nio.charset.Charset;
import java.util.Objects;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HttpHandler extends SimpleChannelInboundHandler<CoustomHttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(HttpHandler.class);
    @Autowired
    NettyConfig config;
    @Autowired
    SpringUtils springUtils;
    @Autowired
    //打算实现Spring多种路径,后期做优化
    RouteBeans route;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CoustomHttpRequest request) throws Exception {
        FullHttpResponse response=null;
        HttpVersion httpVersion = HttpVersion.HTTP_1_1;
        Charset charset = CharsetUtil.UTF_8;
        HttpResponseStatus responseStatus = HttpResponseStatus.OK;
        ByteBuf content=null;
        String uri = request.getRequest().uri();
        RequestMethod method = RequestMethod.getName(request.getRequest().method().name());
        HttpLate late = new HttpLate(uri,request.getRequest().method().name());
        if(!uri.startsWith(config.getContentPath())){
            responseStatus = HttpResponseStatus.NOT_FOUND;
            content = Unpooled.copiedBuffer(responseStatus.toString(),charset);
        }else{
            uri = uri.substring(config.getContentPath().length());
            logger.info("This requeest  uri:[ {} ] method:[ {} ]",uri,late.getMethod());
            RequestMappingInfo rmi = null;
            if(method==RequestMethod.GET){
                rmi = route.getRoute(uri, method,null);
            }else {
                rmi = route.getRoute(uri,method,request.getContentType());
            }
            if(!Objects.isNull(rmi)){
                Object obj = springUtils.getBean(rmi.getBeanName());
                Object restObj = obj.getClass().getDeclaredMethod(rmi.getExecMethod().getName()).invoke(obj);
                content = Unpooled.copiedBuffer(restObj.toString(), Charset.forName(config.getCharset()));
            }else {
                sendError(ctx, HttpResponseStatus.NOT_FOUND);
                return;
            }
        }
        response = new DefaultFullHttpResponse(httpVersion, responseStatus,content);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
        ctx.writeAndFlush(response);
    }
    /**
     * 发送错误信息
     * @param ctx ChannelHandlerContext
     * @param status HttpResponseStatus
     */
    private void sendError(ChannelHandlerContext ctx,
                           HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                status, Unpooled.copiedBuffer("Failure: " + status.toString()
                + "\r\n", Charset.forName(config.getCharset())));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
