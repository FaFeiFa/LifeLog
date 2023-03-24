package com.hua.project.project_first.service.registerAndLoginService;

import com.hua.project.project_first.dao.UserMapper;
import com.hua.project.project_first.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.util.Random;

@Service
@Slf4j
public class RegisterService {

    @Autowired
    UserMapper userMapper;

    public int sendMail(String to) throws Exception {
        MSGservice msGservice = new MSGservice();
        Random random = new Random();
        int i = random.nextInt(10_000_000);
        String text = "您的验证码为"+i+"，请勿泄露给他人，如果不是你本人操作，请忽略此邮件。";
        String title = "验证码服务";
        msGservice.sendMail(to,text,title);
        log.info("账户:{}&& 验证码:{}",to,i);
        return i;
    }
    public boolean save(User user){
        try {
            userMapper.setUser(user);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
