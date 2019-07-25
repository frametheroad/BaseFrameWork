package com.frame;

import com.frame.config.NettyConfig;
import com.frame.lication.NettyHttpServerLication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
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
        httpServerLication.run();
    }
}
