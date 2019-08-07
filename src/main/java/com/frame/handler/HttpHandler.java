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
import com.frame.utils.StringUtils;
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
import java.lang.reflect.Parameter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
/**
 * workerGroup处理管道Channel
 */
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
        //参数定义
        FullHttpResponse response = null;
        HttpVersion httpVersion = HttpVersion.HTTP_1_1;
        Charset charset = CharsetUtil.UTF_8;
        HttpResponseStatus responseStatus = HttpResponseStatus.OK;
        ByteBuf content = Unpooled.copiedBuffer("", Charset.forName(config.getCharset()));
        Object restObj = null;
        String uri = request.getRequest().uri();
        RequestMethod method = RequestMethod.getName(request.getRequest().method().name());
        //判断是否传入项目跟路径  http://localhost:port/项目跟路径
        if (!uri.startsWith(config.getContentPath())) {
            //返回404
            sendError(ctx,HttpResponseStatus.NOT_FOUND);
            return;
        }
        else {
            uri = uri.substring(config.getContentPath().length());
            logger.info("This requeest  uri:[ {} ] method:[ {} ]", uri, method.getName());
            RequestMappingInfo rmi = null;
            //判断Http请求方式是GET还是非GET 获取路由对象
            if (method == RequestMethod.GET) {
                rmi = route.getRoute(uri, method, null);
            } else {
                rmi = route.getRoute(uri, method, request.getContentType());
            }
            //判断获取路由对象是否为空，为空直接报错
            if (!Objects.isNull(rmi)) {
                Method method1 = rmi.getExecMethod();
                //获取路由执行方法参数集合
                Parameter[] parameters = method1.getParameters();
                //获取路由执行方法参数列表
                Class<?>[] paramentersType = method1.getParameterTypes();
                //根据BeanFactory获取路由Class
                Object obj = springUtils.getBean(rmi.getBeanName());
                //没有参数时直接执行
                if (paramentersType.length == 0) {
                    //没有参数是直接执行方法
                    restObj = obj.getClass().getDeclaredMethod(rmi.getExecMethod().getName()).invoke(obj);
                } else if (paramentersType.length == 1) { //只有一个参数时传参
                    //根据Content-Type判断是json还是xml
                    switch (HttpUtils.getContentTypeMapping(request.getContentType())) {
                        case "json":
                            //反射机制
                            Object pto = JSON.parseObject(request.getBody().toString(), paramentersType[0]);
                            restObj = obj.getClass().getDeclaredMethod(rmi.getExecMethod().getName(),paramentersType).invoke(obj, pto);
                            break;
                        case "xml":
                            //获取对应参数
                            JSONObject xmlJson = ((JSONObject) request.getBody());
                            String soapMethod = xmlJson.getJSONObject("Head").getString("ServiceAction").replace("run/", "");
                            JSONObject xmlBodyJson = xmlJson.getJSONObject("Body").getJSONObject("Req" + soapMethod);

                            //反射机制
                            Object pto1 = JSON.parseObject(xmlBodyJson.getJSONObject("SvcBody").toJSONString(), paramentersType[0]);
                            restObj = obj.getClass().getDeclaredMethod(rmi.getExecMethod().getName(),paramentersType).invoke(obj, pto1);
                            break;
                        case "string":
                            break;
                        default:
                            break;
                    }
                } else {//多个参数时

                    JSONObject jsonObject = new JSONObject(true);
                    switch (HttpUtils.getContentTypeMapping(request.getContentType())) {
                        case "json":
                            jsonObject.putAll((JSONObject)request.getBody());
                            Object[] parObjs = new Object[parameters.length];
                            for(int pIndex = 0; pIndex<parameters.length;pIndex++){
                                if(parameters[pIndex].getType() == String.class){
                                    parObjs[pIndex] = jsonObject.containsKey(parameters[pIndex].getName())?jsonObject.get(parameters[pIndex].getName()):"";
                                }else if(parameters[pIndex].getType() == Integer.class){
                                    parObjs[pIndex] = jsonObject.containsKey(parameters[pIndex].getName())?jsonObject.get(parameters[pIndex].getName()):0;
                                }

                            }
                            restObj = obj.getClass().getDeclaredMethod(rmi.getExecMethod().getName(),paramentersType).invoke(obj, parObjs);
                            break;
                        case "xml":
                            JSONObject xmlJson = ((JSONObject) request.getBody());
                            String soapMethod = xmlJson.getJSONObject("Head").getString("ServiceAction").replace("run/", "");
                            jsonObject.putAll(xmlJson.getJSONObject("Body").getJSONObject("Req"+soapMethod).getJSONObject("SvcBody"));
                            jsonObject.put("GloableCode",xmlJson.getJSONObject("Body").getJSONObject("Req"+soapMethod).getJSONObject("SvcHead").getString("GloableCode"));
                            Object[] parxmlObjs = new Object[parameters.length];
                            for(int pIndex = 0; pIndex<parameters.length;pIndex++){
                                if(parameters[pIndex].getType() == String.class){
                                    parxmlObjs[pIndex] = jsonObject.containsKey(StringUtils.toUpperCaseFirstOne(parameters[pIndex].getName()))?jsonObject.getString(StringUtils.toUpperCaseFirstOne(parameters[pIndex].getName())):"";
                                }else if(parameters[pIndex].getType() == Integer.class){
                                    parxmlObjs[pIndex] = jsonObject.containsKey(StringUtils.toUpperCaseFirstOne(parameters[pIndex].getName()))?jsonObject.getInteger(StringUtils.toUpperCaseFirstOne(parameters[pIndex].getName())):0;
                                }

                            }
                            restObj = obj.getClass().getDeclaredMethod(rmi.getExecMethod().getName(),paramentersType).invoke(obj, parxmlObjs);
                            break;
                        case "string":
                            break;
                        default:
                            break;
                    }
//                    JSONObject jsonObject = ((JSONObject) request.getBody());

                }

            } else {
                sendError(ctx, HttpResponseStatus.NOT_FOUND);
                return;
            }
            response = new DefaultFullHttpResponse(httpVersion, responseStatus, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, request.getContentType());
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            CoustomHttpResponse chr = new CoustomHttpResponse(response, restObj);
            String[] soapType = {"application/xml", "text/xml"};
            if (Arrays.asList(soapType).contains(request.getContentType())) {
                String soapMethod = ((JSONObject) request.getBody()).getJSONObject("Head").getString("ServiceAction");
                chr.setSoapMethod(soapMethod.replace("run/", ""));
            }
            ctx.writeAndFlush(chr);
        }
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
