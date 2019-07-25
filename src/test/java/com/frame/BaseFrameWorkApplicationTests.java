package com.frame;

import com.frame.controller.UserLoginController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseFrameWorkApplicationTests {
    @Autowired
    UserLoginController userLoginController;
    @Test
    public void userList() {
        userLoginController.list();
    }

}
