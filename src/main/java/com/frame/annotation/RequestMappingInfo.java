package com.frame.annotation;


import java.lang.reflect.Method;

public class RequestMappingInfo {
    private String beanName;
    private Method execMethod;
    private String name;

//    private String[] value;
    private String value;
    private String[] path;

    private RequestMethod[] method;

    private String[] params;

    private String[] headers;

    private String[] consumes;

    private String[] produces;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Method getExecMethod() {
        return execMethod;
    }

    public void setExecMethod(Method execMethod) {
        this.execMethod = execMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String[] getPath() {
        return path;
    }

    public void setPath(String[] path) {
        this.path = path;
    }

    public RequestMethod[] getMethod() {
        return method;
    }

    public void setMethod(RequestMethod[] method) {
        this.method = method;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public String[] getConsumes() {
        return consumes;
    }

    public void setConsumes(String[] consumes) {
        this.consumes = consumes;
    }

    public String[] getProduces() {
        return produces;
    }

    public void setProduces(String[] produces) {
        this.produces = produces;
    }
}
