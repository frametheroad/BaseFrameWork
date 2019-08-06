package com.frame.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.frame.annotation.RequestMappingInfo;
import com.frame.annotation.RequestMethod;
import com.frame.bean.RouteBeans;
import com.frame.codec.CoustomHttpRequest;
import com.frame.codec.CoustomHttpResponse;
import com.frame.config.NettyConfig;
import com.frame.utils.HttpUtils;
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

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;
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
        ByteBuf content=Unpooled.copiedBuffer("",Charset.forName(config.getCharset()));
        Object restObj = null;
        String uri = request.getRequest().uri();
        RequestMethod method = RequestMethod.getName(request.getRequest().method().name());
        if(!uri.startsWith(config.getContentPath())){
            responseStatus = HttpResponseStatus.NOT_FOUND;
            content = Unpooled.copiedBuffer(responseStatus.toString(),charset);
        }else{
            uri = uri.substring(config.getContentPath().length());
            logger.info("This requeest  uri:[ {} ] method:[ {} ]",uri,method.getName());
            RequestMappingInfo rmi = null;
            if(method==RequestMethod.GET){
                rmi = route.getRoute(uri, method,null);
            }else {
                rmi = route.getRoute(uri,method,request.getContentType());
            }
            if(!Objects.isNull(rmi)){
                Method method1 = rmi.getExecMethod();
                Class<?>[] paramentersType = method1.getParameterTypes();
                Class<?>[] commType = {String.class,Integer.class};
                Object obj = springUtils.getBean(rmi.getBeanName());
                //没有参数时直接执行
                if(paramentersType.length==0){
                    obj.getClass().getDeclaredMethod(rmi.getExecMethod().getName()).invoke(obj);
                }else if(paramentersType.length==1){ //只有一个参数时传参
                    switch (HttpUtils.getContentTypeMapping(request.getContentType())) {
                        case "json":
                            Object pto = JSON.parseObject(request.getBody().toString(),paramentersType[0]);
                            restObj = obj.getClass().getDeclaredMethod(rmi.getExecMethod().getName()).invoke(obj,pto);
                            break;
                        case "xml":
                            JSONObject xmlJson = ((JSONObject)request.getBody());
                            String soapMethod = xmlJson.getJSONObject("Head").getString("ServiceAction").replace("run/","");
                            JSONObject xmlBodyJson = xmlJson.getJSONObject("Body").getJSONObject("Req"+soapMethod);
                            Object pto = JSON.parseObject(xmlBodyJson.getJSONObject("SvcBody").toJSONString(),paramentersType[0]);
                            restObj = obj.getClass().getDeclaredMethod(rmi.getExecMethod().getName()).invoke(obj,pto);
                            break;
                        case "string":
                            break;
                        default:
                            break;
                    }{
                }else{//多个参数时

                }

            }else {
                sendError(ctx, HttpResponseStatus.NOT_FOUND);
                return;
            }
        }
        response = new DefaultFullHttpResponse(httpVersion, responseStatus,content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,request.getContentType());
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
        CoustomHttpResponse chr = new CoustomHttpResponse(response,restObj);
        String[] soapType ={"application/xml","text/xml"};
        if(Arrays.asList(soapType).contains(request.getContentType())){
            String soapMethod = ((JSONObject)request.getBody()).getJSONObject("Head").getString("ServiceAction");
            chr.setSoapMethod(soapMethod.replace("run/",""));
        }
        ctx.writeAndFlush(chr);
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
