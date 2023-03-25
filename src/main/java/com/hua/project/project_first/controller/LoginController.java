package com.hua.project.project_first.controller;

import com.hua.project.project_first.pojo.Code;
import com.hua.project.project_first.pojo.ReMsg;
import com.hua.project.project_first.pojo.User;
import com.hua.project.project_first.service.redisService.RegisterRedisService;
import com.hua.project.project_first.service.registerAndLoginService.EncryptService;
import com.hua.project.project_first.service.registerAndLoginService.LoginServer;
import com.hua.project.project_first.service.registerAndLoginService.TokenService;
import com.hua.project.project_first.utils.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@ResponseBody
public class LoginController {

    @Autowired
    LoginServer loginServer;
    @Autowired
    EncryptService encryptService;
    @Autowired
    TokenService tokenService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * /login接口,用于登录和颁发token(14天)
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ReMsg login(@RequestParam("email") String email,
                       @RequestParam("password") String password) {
        password = encryptService.getResult(email + password);
        User user = loginServer.login(email, password);
        if (user != null) {
            log.info("id为{}的用户{}登录了", user.getId(), user.getNickname());
            String token = tokenService.issuedToken(user);
            stringRedisTemplate.opsForValue().set(email+"_SERVER_TOKEN", token);
            stringRedisTemplate.expire(email+"_SERVER_TOKEN", 14, TimeUnit.DAYS);
            return new ReMsg(Code.OK_200.toString(),null, MapUtil.GetMap("SERVER_TOKEN",token));
        }
        return new ReMsg(Code.NotFound_404.toString(),"用户不存在",null);
    }
}
