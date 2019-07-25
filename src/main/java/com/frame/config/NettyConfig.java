package com.frame.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.netty")
public class NettyConfig {
    private int port;
    private String contentPath;
    private int bossThread;
    private int workerThread;
    private boolean keepalive;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    public int getBossThread() {
        return bossThread;
    }

    public void setBossThread(int bossThread) {
        this.bossThread = bossThread;
    }

    public int getWorkerThread() {
        return workerThread;
    }

    public void setWorkerThread(int workerThread) {
        this.workerThread = workerThread;
    }

    public boolean isKeepalive() {
        return keepalive;
    }

    public void setKeepalive(boolean keepalive) {
        this.keepalive = keepalive;
    }
}

