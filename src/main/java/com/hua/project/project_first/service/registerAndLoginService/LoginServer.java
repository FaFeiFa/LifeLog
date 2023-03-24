package com.hua.project.project_first.service.registerAndLoginService;

import com.hua.project.project_first.dao.UserMapper;
import com.hua.project.project_first.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j

public class LoginServer {
    @Autowired
    UserMapper userMapper;
    public User login(String email , String password){
        User user = userMapper.equalUser(email, password);
        return user;
    }
}
