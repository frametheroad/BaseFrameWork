package com.frame.utils;

import com.frame.config.JedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
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

    private static Logger logger = LoggerFactory.getLogger(JedisFactory.class);

    @Bean
    public JedisCluster getJedisCluster(){
        String[] serverArray=jedisConfig.getClusterNodes().split(",");
        Set<HostAndPort> nodes=new HashSet<>();

        for (String ipPort:serverArray){
            String [] ipPortPair=ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(),Integer.valueOf(ipPortPair[1].trim())));

        }
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(jedisConfig.getMaxIdle());
        jedisPoolConfig.setMinIdle(jedisConfig.getMinIdle());
        jedisPoolConfig.setMaxTotal(jedisConfig.getMaxActive());
        JedisCluster jedisCluster = new JedisCluster(nodes,jedisConfig.getCommandTimeout(),jedisPoolConfig);
        logger.info("jedisCluster注入成功！");
        logger.info(""+jedisConfig.getMaxIdle());
        return jedisCluster;
    }

}
