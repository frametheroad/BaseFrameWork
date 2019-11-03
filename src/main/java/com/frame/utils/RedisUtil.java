package com.frame.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author: yinxin
 * @description: redis工具类
 * @create: 2019-10-26 9:58
 **/
@Component
public class RedisUtil<main> {

    private static Logger log = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private JedisFactory jedisFactory1;

    private static JedisFactory jedisFactory;

    @PostConstruct
    public void init() {
        jedisFactory = jedisFactory1;
    }


    public static boolean setToRedis(String key,Object value){
        try {
            String str=jedisFactory.getJedisCluster().set(key, String.valueOf(value));
            if("OK".equals(str))
                return true;
        }catch (Exception ex){
            log.error("setToRedis:{Key:"+key+",value"+value+"}",ex);
        }
        return false;
    }

    public static Object getRedis(String key){
        String str=null;
        try {
            str=jedisFactory.getJedisCluster().get(key);
        }catch (Exception ex){
            log.error("getRedis:{Key:"+key+"}",ex);
        }
        return str;
    }

    public static void main(String[] args) {
        System.out.println(jedisFactory);
    }

}
