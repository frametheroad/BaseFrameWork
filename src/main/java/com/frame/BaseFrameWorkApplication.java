package com.frame;

import com.frame.lication.NettyHttpServerLication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.frame","com.service"})
public class BaseFrameWorkApplication implements CommandLineRunner {
    @Autowired
    NettyHttpServerLication httpServerLication;
    public static void main(String[] args) {
        SpringApplication.run(BaseFrameWorkApplication.class, args);
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        //启动Netty服务
        httpServerLication.run();
    }
}
