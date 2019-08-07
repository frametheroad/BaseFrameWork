package com.service.controller;

import com.frame.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * @PackgeName:com.service.controller
 * @ClassName:TestController
 * @Auther: 马俊
 * @Date: 2019-08-07 16:10
 * @Description:
 */
@Controller
public class TestController {
    @RequestMapping(value = "/test/one")
    public void one(){

    }
}
