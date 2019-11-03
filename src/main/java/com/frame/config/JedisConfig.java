package com.frame.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//@Component 添加这个注释方便Spring扫描器
@Component
//加载配置文件配置,通过Set方法注入对象
@ConfigurationProperties(prefix = "spring.redis.cache")
public class JedisConfig {

    private String clusterNodes;//集群节点

    private int commandTimeout;//连接超时时间

    private int maxActive;//连接池最大连接数

    private int maxWait;//连接池最大阻塞等待时间（使用负值表示没有限制）

    private int maxIdle;//连接池中的最大空闲连接

    private int minIdle;//接池中的最小空闲连接

    public String getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    public int getCommandTimeout() {
        return commandTimeout;
    }

    public void setCommandTimeout(int commandTimeout) {
        this.commandTimeout = commandTimeout;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

}
