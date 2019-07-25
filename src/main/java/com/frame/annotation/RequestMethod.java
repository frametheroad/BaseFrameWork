package com.frame.annotation;

public enum RequestMethod {
    GET("GET",1), HEAD("GET",1), POST("GET",1), PUT("GET",1), PATCH("GET",1), DELETE("GET",1), OPTIONS("GET",1), TRACE("GET",1);
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
    }
