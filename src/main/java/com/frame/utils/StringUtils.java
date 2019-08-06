package com.frame.utils;

import org.springframework.stereotype.Component;

/**
 * @PackgeName:com.frame.utils
 * @ClassName:StringUtils
 * @Auther: 马俊
 * @Date: 2019-08-04 20:00
 * @Description:
 */
@Component
public class StringUtils {
    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if(null==str||str.trim().length()==0||str.trim().equals("")){
            return true;
        }
        return false;
    }

    /**
     * 字符串首字母转大写
     * @param s
     * @return
     */
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
}
