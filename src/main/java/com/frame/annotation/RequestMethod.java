package com.frame.annotation;

public enum RequestMethod {
    GET("GET",1), HEAD("HEAD",2), POST("POST",3), PUT("PUT",4), PATCH("PATCH",6), DELETE("DELETE",7), OPTIONS("OPTIONS",8), TRACE("TRACE",9);
    private String name;
    private int index;
    // 构造方法
    private RequestMethod(String name, int index) {
        this.name = name;
        this.index = index;
    }
    // 普通方法
    public static String getName(int index) {
        for (RequestMethod c : RequestMethod.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
    // get set 方法
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public static RequestMethod getName(String name){
        for (RequestMethod c : RequestMethod.values()) {
            if (c.getName() == name) {
                return c;
            }
        }
        return null;
    }
    }
