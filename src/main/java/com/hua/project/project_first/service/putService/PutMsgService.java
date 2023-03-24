package com.hua.project.project_first.service.putService;

import com.hua.project.project_first.dao.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PutMsgService {
    @Autowired
    UserMapper userMapper;
    public void rePassword(String password , String email){
        userMapper.rePassword(password,email);
        log.info("email为{}的用户修改了密码",email);
    }
}
