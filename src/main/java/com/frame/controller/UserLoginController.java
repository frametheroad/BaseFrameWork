package com.frame.controller;

import com.alibaba.fastjson.JSONObject;
import com.frame.EntityVo;
import com.frame.annotation.RequestMapping;
import com.frame.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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

    @RequestMapping(value = "/user/set",method = {RequestMethod.POST},headers = {"content-type:application/xml","content-type:text/xml"})
    public EntityVo get(String name,String sex,Integer age){
        return new EntityVo(name,String.valueOf(sex),String.valueOf(age));
    }
    public void setParament(EntityVo ev){
        System.out.println(JSONObject.toJSONString(ev));
    }
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Object obj = new UserLoginController();
        for (Method method : obj.getClass().getDeclaredMethods()){
            if(method.getName().equals("get")){
                Class<?>[] clazzs = method.getParameterTypes();
                Parameter[] parameters = method.getParameters();
                for (Parameter p: parameters){
                    Class<?> t = p.getType();
                    String key = p.getName();
                }
                JSONObject json = JSONObject.parseObject("{\"name\":\"马军\",\"sex\":\"男\",\"age\":\"13\"}");
                Object[] parObjs = new Object[parameters.length];
                for (int pIndex = 0 ; pIndex<parameters.length;pIndex++) {
                    if (parameters[pIndex].getType() == String.class){
                        parObjs[pIndex] = json.getString(parameters[pIndex].getName());
                        System.out.println(parameters[pIndex].getName());
                    }else if(parameters[pIndex].getType() == Integer.class){
                        parObjs[pIndex] = json.getInteger(parameters[pIndex].getName());
                        System.out.println(parameters[pIndex].getName());
                    }
                }
                System.out.println(JSONObject.toJSONString(method.invoke(obj,parObjs)));
            }
        }
        System.out.println();
    }

}
