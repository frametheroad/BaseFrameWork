package com.frame.utils;

import org.springframework.stereotype.Component;

@Component
public class HttpUtils {
    /**
     * 根据 Http head Content-Type 获取需要解析的类型
     * @param contentType Http Content-Type
     * @return type = {json,xml,string,""}
     */
    public static String getContentTypeMapping(String contentType){
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
}
