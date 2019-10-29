package com.frame.controller;

import com.frame.EntityVo;
import com.frame.annotation.RequestMapping;
import com.frame.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class QueryRedisController {
    @RequestMapping(value = "/redis/keys",headers = {"content-type:application/xml","content-type:text/xml"},method = RequestMethod.POST)
    public List<EntityVo> keys(){
        List<EntityVo> list = new ArrayList<>();
        list.add(new EntityVo("zhangsan","男","13"));
        return list;
    }
}
