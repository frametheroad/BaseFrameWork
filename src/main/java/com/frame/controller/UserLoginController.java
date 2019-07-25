package com.frame.controller;

import com.frame.annotation.RequestMapping;
import com.frame.annotation.RequestMethod;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
public class UserLoginController {
    @RequestMapping(value = "/user/list",method = {RequestMethod.GET})
    public String list(){
    return "恭喜你进入了查询用户的方法";
    }
}
