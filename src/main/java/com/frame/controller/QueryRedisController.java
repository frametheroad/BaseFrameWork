package com.frame.controller;

import com.frame.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
public class QueryRedisController {
    @RequestMapping(value = "/redis/keys")
    public String keys(){
        return "恭喜你进入/redis/keys";
    }
}
