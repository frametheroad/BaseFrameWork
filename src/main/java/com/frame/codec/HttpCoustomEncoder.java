package com.frame.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.frame.utils.HttpUtils;
import com.frame.utils.StringUtils;
import com.frame.utils.XMLUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import org.dom4j.*;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * @PackgeName:com.frame.codec
 * @ClassName:HttpCoustomEncoder
 * @Auther: 马俊
 * @Date: 2019-08-06 13:48
 * @Description:
 */
public class HttpCoustomEncoder extends AbstracHttpCoustomEncoder<CoustomHttpResponse> {

    public HttpCoustomEncoder(boolean isPrint) {
        super(isPrint);
    }

    public HttpCoustomEncoder(boolean isPrint, Charset charset) {
        super(isPrint, charset);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CoustomHttpResponse msg, List<Object> out) throws Exception {
        try {
            String bodyTmp = "";
            switch (HttpUtils.getContentTypeMapping(msg.getResponse().headers().get(HttpHeaderNames.CONTENT_TYPE))) {
                case "json":
                    bodyTmp = object2JSONString(msg.getBody());
                    break;
                case "xml":
                    JSONObject jsonObject = new JSONObject(true);
                    Random r = new Random();
                    JSONObject head = new JSONObject(true);
                    head.put("Mac", new XMLMiddleBean(UUID.randomUUID().toString(),"d"));
                    head.put("MacTime", new XMLMiddleBean(String.valueOf(System.currentTimeMillis()),"d"));
                    head.put("ServiceAction",new XMLMiddleBean("run/"+msg.getSoapMethod(),"d"));
                    jsonObject.put("Head",new XMLMiddleBean(head,"soap"));
                    JSONObject body = new JSONObject(true);
                    JSONObject svcHead = new JSONObject(true);
                    svcHead.put("SysCode",new XMLMiddleBean(String.valueOf(r.nextInt()),"s13"));
                    svcHead.put("GloableCode",new XMLMiddleBean(String.valueOf(r.nextInt()),"s13"));
                    JSONObject sendMsgSvc = new JSONObject(true);
                    sendMsgSvc.put("ResSvcHead",new XMLMiddleBean(svcHead,"d",true));
                    JSONObject svcBody = new JSONObject(true);
                    object2XMLString(msg.getBody(),svcBody);
                    sendMsgSvc.put("ResSvcBody",new XMLMiddleBean(svcBody.containsKey("frameArrays")?svcBody.getJSONArray("frameArrays"):svcBody,"d",true));
                    body.put(msg.getSoapMethod(),new XMLMiddleBean(sendMsgSvc,"Req",false));
                    jsonObject.put("Body",new XMLMiddleBean(body,"soap"));
//                    bodyTmp = jsonObject.toJSONString();
                    Document doc = DocumentHelper.createDocument();
                    doc.setXMLEncoding("UTF-8");
                    Element rootElement = doc.addElement(new QName("Envelope",XMLUtils.namespaces.get("soap")));
                    for(Namespace ns : XMLUtils.namespaces.values()){
                        rootElement.addNamespace(ns.getPrefix(),ns.getURI());
                    }
                    XMLUtils.convertJsonToXml(rootElement,jsonObject);
                    bodyTmp = rootElement.asXML();
                    break;
                case "string":
                    bodyTmp = object2String(msg.getBody());
                    break;
                default:
                    break;
            }
            ByteBuf buf = encode0(ctx, bodyTmp);
            FullHttpResponse response = new DefaultFullHttpResponse(msg.getResponse().protocolVersion(), msg.getResponse().status(), buf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, msg.getResponse().headers().get(HttpHeaderNames.CONTENT_TYPE));
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
            out.add(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private String object2JSONString(Object obj){
        if (Objects.isNull(obj)){
            return "{}";
        }else if(obj instanceof JSONObject){
            return ((JSONObject)obj).toJSONString();
        }else{
            return JSONObject.toJSONString(obj);
        }
    }
    private void object2XMLString(Object obj,JSONObject jsonObject){
        String body = "";
        if(Objects.isNull(obj)){
            body="";
        }else if (obj instanceof JSONObject){
            JSONObject json = (JSONObject)obj;
            for (String key : json.keySet()){
                if(! (json.get(key) instanceof JSONObject) && !(json.get(key) instanceof JSONArray)){
                    XMLMiddleBean xmlMiddleBean = new XMLMiddleBean(json.getString(key),"s",true);
                    jsonObject.put(StringUtils.toUpperCaseFirstOne(key),xmlMiddleBean);
                }else if(json.get(key) instanceof JSONObject){
                    JSONObject jsonTmp = new JSONObject(true);
                    object2XMLString(json.get(key),jsonTmp);
                    jsonObject.put(StringUtils.toUpperCaseFirstOne(key),jsonTmp);
                }else{
                    JSONArray arrloop = json.getJSONArray(key);
                    JSONArray arrTmp = new JSONArray();
                    for (int arrIndex = 0 ; arrIndex < arrloop.size() ; arrIndex++){
                        JSONObject jsonTmp = new JSONObject(true);
                        object2XMLString(arrloop.get(arrIndex),jsonTmp);
                        arrTmp.add(jsonTmp);
                    }
                jsonObject.put(StringUtils.toUpperCaseFirstOne(key),new XMLMiddleBean(arrTmp,"s",true));
                }
            }
//            return "";
        }else{
            Object ob = JSON.parse(JSON.toJSONString(obj));
            if(ob instanceof JSONArray){
                JSONArray arrayTemp = (JSONArray)ob;
                JSONArray arrRest = new JSONArray();
                for(int arrIndex = 0 ; arrIndex < arrayTemp.size();arrIndex++){
                    JSONObject jsonTemp = new JSONObject(true);
                    object2XMLString(arrayTemp.get(arrIndex),jsonTemp);
                    arrRest.add(jsonTemp);
                }
                jsonObject.put("frameArrays",arrRest);
            }else{
                object2XMLString(JSON.parseObject(JSON.toJSONString(obj)),jsonObject);
            }
        }
    }

    private String object2String(Object obj){
        if(Objects.isNull(obj)){
            return "";
        }else{
            return obj.toString();
        }
    }
}
