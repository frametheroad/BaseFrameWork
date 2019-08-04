package com.frame.codec;

public class XMLMiddleBean {
    private Object value;
    private String  prefix;
    private boolean split;
    private XMLMiddleBean(){

    }
    public XMLMiddleBean(Object value){
        this(value,null);
    }

    public XMLMiddleBean(Object value, String prefix) {
        this(value,prefix,true);
    }

    public XMLMiddleBean(Object value, String prefix, boolean split) {
        this.value = value;
        this.prefix = prefix;
        this.split = split;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isSplit() {
        return split;
    }

    public void setSplit(boolean split) {
        split = split;
    }
}
