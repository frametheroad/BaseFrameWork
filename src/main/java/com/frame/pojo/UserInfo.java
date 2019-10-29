package com.frame.pojo;

import org.springframework.data.annotation.Id;

/**
 * Created by wuming on 2019/10/18.
 */
public class UserInfo {
    @Id
    private String id;
    private String userName;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserInfo(String id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public UserInfo(String id) {
        this.id = id;
    }
}
