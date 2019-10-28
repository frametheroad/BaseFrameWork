package com.frame.utils;

import com.frame.config.JedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: yinxin
 * @description:
 * @create: 2019-07-31 18:16
 **/
@Component
public class JedisFactory {

    @Autowired
    private JedisConfig jedisConfig;

    public JedisCluster getJedisCluster(){
        String[] serverArray=jedisConfig.getClusterNodes().split(",");
        Set<HostAndPort> nodes=new HashSet<>();

        for (String ipPort:serverArray){
            String [] ipPortPair=ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(),Integer.valueOf(ipPortPair[1].trim())));

        }
        return new JedisCluster(nodes,jedisConfig.getCommandTimeout());
    }

}
