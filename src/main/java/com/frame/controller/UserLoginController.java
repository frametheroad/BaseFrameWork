package com.frame.controller;

import com.frame.EntityVo;
import com.frame.annotation.RequestMapping;
import com.frame.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserLoginController {
    @RequestMapping(value = "/user/list",method = {RequestMethod.GET})
    public String list(){
        return "恭喜你进入了查询用户的方法";
    }
    @RequestMapping(value = "/user/queryAll",method = {RequestMethod.POST},headers = {"content-type:application/xml"})
    public List<EntityVo> queryAll(){
        List<EntityVo> list = new ArrayList<>();
        list.add(new EntityVo("zhangsan","男","13"));
        list.add(new EntityVo("zhangsan","男","15"));
        list.add(new EntityVo("zhangsan","男","17"));
        list.add(new EntityVo("zhangsan","男","22"));
        list.add(new EntityVo("zhangsan","男","14"));
        return list;
    }
}
