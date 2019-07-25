package com.frame.handler.bean;

public class HttpLate {
    private String uri;
    private String method;
    private HttpLate(){

    }
    public HttpLate(String uri,String method){
        this.uri=uri;
        this.method=method;
    }
    public String getUri() {
        return uri;
    }


    public String getMethod() {
        return method;
    }

}
