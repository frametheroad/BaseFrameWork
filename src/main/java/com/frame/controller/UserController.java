package com.frame.controller;

import com.frame.annotation.RequestMapping;
import com.frame.annotation.RequestMethod;
import com.frame.pojo.UserInfo;
import com.frame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Created by wuming on 2019/10/18.
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/save",method = {RequestMethod.GET})
    public String save(){
        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(),"用户"+System.currentTimeMillis(),"123");
        userService.save(userInfo);
        return "success";
    }


    @RequestMapping(value = "/getUserList",method = {RequestMethod.POST})
    public List<UserInfo> getUserList(){
        List<UserInfo> userInfoList = userService.findAll();
        return userInfoList;
    }

    @RequestMapping(value = "/delete",method = {RequestMethod.GET})
    public String delete(String id){
        UserInfo userInfo = new UserInfo(id);
        userService.delete(userInfo);
        return "success";
    }

    @RequestMapping(value = "/update",method = {RequestMethod.GET})
    public String update(String id,String username,String password){
        UserInfo userInfo = new UserInfo(id,username,password);
        userService.save(userInfo);
        return "success";
    }
}
