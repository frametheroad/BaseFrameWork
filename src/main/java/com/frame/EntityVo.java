package com.frame;

import java.io.Serializable;

/**
 * @PackgeName:com.frame
 * @ClassName:entity
 * @Auther: 马俊
 * @Date: 2019-08-06 16:20
 * @Description:
 */
public class EntityVo implements Serializable {
    private String name;
    private String sex;
    private String age;

    public EntityVo(String name, String sex, String age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
