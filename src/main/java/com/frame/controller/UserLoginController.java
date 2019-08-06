package com.frame.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.frame.EntityVo;
import com.frame.annotation.RequestMapping;
import com.frame.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserLoginController {
    @RequestMapping(value = "/user/list",method = {RequestMethod.GET})
    public String list(){
        return "恭喜你进入了查询用户的方法";
    }
    @RequestMapping(value = "/user/queryAll",method = {RequestMethod.POST},headers = {"content-type:application/xml","content-type:text/xml"})
    public List<EntityVo> queryAll(){
        List<EntityVo> list = new ArrayList<>();
        list.add(new EntityVo("zhangsan","男","13"));
        list.add(new EntityVo("zhangsan","男","15"));
        list.add(new EntityVo("zhangsan","男","17"));
        list.add(new EntityVo("zhangsan","男","22"));
        list.add(new EntityVo("zhangsan","男","14"));
        return list;
    }
    @RequestMapping(value = "/user/getOne",method = {RequestMethod.POST},headers = {"content-type:application/xml","content-type:text/xml"})
    public EntityVo getOne(){
        return new EntityVo("mj","男","33");
    }
    public void setParament(EntityVo ev){
        System.out.println(JSONObject.toJSONString(ev));
    }
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Object obj = new UserLoginController();
        for (Method method : obj.getClass().getDeclaredMethods()){
            if(method.getName().equals("setParament")){
                Class<?>[] clazzs = method.getParameterTypes();
                JSONObject json = JSONObject.parseObject("{\"name\":\"马军\",\"sex\":\"男\",\"age\":\"13\"}");
                method.invoke(obj, JSON.parseObject(json.toJSONString(),clazzs[0]));
                System.out.println();
            }
        }
        System.out.println();
    }
}
