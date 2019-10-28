package com.frame.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@Component 添加这个注释方便Spring扫描器
@Component
//加载配置文件配置,通过Set方法注入对象
@ConfigurationProperties(prefix = "spring.redis.cache")
public class JedisConfig {

    private String clusterNodes;

    private int commandTimeout;

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
}
