package com.frame;

import com.frame.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: yinxin
 * @description: 4
 * @create: 2019-10-27 17:06
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Test
    public void printRedis(){
        System.out.println(RedisUtil.getRedis("111"));
    }
}
