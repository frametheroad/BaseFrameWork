package com.frame.controller;

import com.frame.annotation.RequestMapping;
import com.frame.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

@Controller
public class QueryRedisController {
    @RequestMapping(value = "/redis/keys",headers = {"content-type:application/xml","content-type:text/xml"},method = RequestMethod.POST)
    public String keys(){
        return "恭喜你进入/redis/keys";
    }
}
