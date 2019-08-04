package com.frame.codec;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;

/**
 * @PackgeName:com.frame.codec
 * @ClassName:CoustomHttpRequest
 * @Auther: 马俊
 * @Date: 2019-08-02 09:28
 * @Description:
 */
public class CoustomHttpRequest {
    private FullHttpRequest request;
    private Object body;
    public CoustomHttpRequest(FullHttpRequest request,Object body){
        this.request=request;
        this.body=body;
    }
    public FullHttpRequest getRequest() {
        return request;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
    public String getContentType(){
        return this.request.headers().get(HttpHeaderNames.CONTENT_TYPE).toLowerCase();
    }
}
