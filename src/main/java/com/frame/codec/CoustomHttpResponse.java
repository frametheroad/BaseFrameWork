package com.frame.codec;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @PackgeName:com.frame.codec
 * @ClassName:CoustomHttpResponse
 * @Auther: 马俊
 * @Date: 2019-08-02 14:28
 * @Description:
 */
public class CoustomHttpResponse {
    private FullHttpResponse response;
    private Object body;
    private CoustomHttpResponse(){

    }
    public CoustomHttpResponse(FullHttpResponse response, Object body){
        this.response=response;
        this.body=body;
    }

    public FullHttpResponse getResponse() {
        return response;
    }

    public void setResponse(FullHttpResponse response) {
        this.response = response;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
