package com.frame.service;

import com.frame.pojo.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by wuming on 2019/10/18.
 */
public interface UserService extends MongoRepository<UserInfo, String> {
}
