package com.frame.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.frame.utils.StringUtils;
import com.frame.utils.XMLUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.nio.charset.Charset;
import java.util.List;
/**
 * @PackgeName:com.frame.codec
 * @ClassName:HttpCoustomDecoder
 * @Auther: 马俊
 * @Date: 2019-08-02 14:31
 * @Description:
 */
public class HttpCoustomDecoder extends AbstracHttpCoustomDecoder<FullHttpRequest>{

    public HttpCoustomDecoder(Class<?> clazz, boolean isPrint) {
        super(clazz, isPrint);
    }

    public HttpCoustomDecoder(Class<?> clazz, boolean isPrint, Charset charset) {
        super(clazz, isPrint, charset);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest request, List<Object> list) throws Exception {
        if(!request.decoderResult().isSuccess()){
            sendError(ctx,HttpResponseStatus.BAD_REQUEST);
            return;
        }
        CoustomHttpRequest cRequest = new CoustomHttpRequest(request, null);
        String contentType = request.headers().get(HttpHeaderNames.CONTENT_TYPE);
        HttpMethod method = request.method();
        String bodyTemp = request.content().toString(Charset.forName("UTF-8"));
        if(method == HttpMethod.GET) {
            String uri = request.uri();
            if(uri.contains("?")) {
                String paramenters = uri.substring(uri.indexOf("?") + 1);
                uri = uri.substring(0, uri.indexOf("?"));
                JSONObject jsonParament = new JSONObject();
                for (String paramenter : paramenters.split("&")) {
                    String[] kv = paramenter.split("=");
                    jsonParament.put(kv[0], kv[1]);
                }
                cRequest.getRequest().setUri(uri);
                cRequest.setBody(jsonParament);
            }else{
                cRequest.setBody(null);
            }
        }else{
            /**
             * 非Get请求，未传入ConetntType视作坏的请求
             */
            if(StringUtils.isEmpty(contentType)){
                sendError(ctx,HttpResponseStatus.BAD_REQUEST);
                return;
            }
            switch (getContentTypeMapping(contentType)) {
                case "json":
                    JSONObject jsonBody = null;
                    try{
                        jsonBody = JSON.parseObject(bodyTemp);
                    }catch (JSONException e){
                        sendError(ctx,HttpResponseStatus.BAD_REQUEST);
                        return;
                    }
                    cRequest.setBody(jsonBody);
                    break;
                case "xml":
                    JSONObject jsonXmlBody = null;
                    try{
                        jsonXmlBody = XMLUtils.documentToJSONObject(bodyTemp);
                    }catch (Exception e){
                        sendError(ctx,HttpResponseStatus.BAD_REQUEST);
                        return;
                    }
                    cRequest.setBody(jsonXmlBody);
                    break;
                case "string":
                    cRequest.setBody(bodyTemp);
                    break;
                default:
                    sendError(ctx, HttpResponseStatus.BAD_REQUEST);
                    break;
            }
        }
        list.add(cRequest);
    }
    /**
     * 根据 Http head Content-Type 获取需要解析的类型
     * @param contentType Http Content-Type
     * @return type = {json,xml,string,""}
     */
    private String getContentTypeMapping(String contentType){
        String ct = contentType.toLowerCase();
        if(ct.startsWith("application/json")){
            return "json";
        }else if(ct.startsWith("application/xml")||ct.startsWith("text/xml")){
            return "xml";
        }else if(ct.startsWith("text/plain")){
            return "string";
        }else{
            return "";
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
                + "\r\n", super.getCharset()));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        ctx.pipeline().writeAndFlush(response);
        //        ctx.writeAndFlush(response);
    }

    public static void main(String[] args) {
        String uri = "/service/abcdefg?who=333&asdas=3434";
        System.out.println(uri.substring(uri.indexOf("?")+1));
        System.out.println(uri.substring(0,uri.indexOf("?")));
    }
}
