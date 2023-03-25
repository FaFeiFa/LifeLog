package com.hua.project.project_first.controller;

import com.hua.project.project_first.pojo.Code;
import com.hua.project.project_first.pojo.ReMsg;
import com.hua.project.project_first.pojo.User;
import com.hua.project.project_first.service.registerAndLoginService.EncryptService;
import com.hua.project.project_first.service.redisService.RegisterRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@ResponseBody
@Slf4j
public class RegisterController {

    @Autowired
    com.hua.project.project_first.service.registerAndLoginService.RegisterService registerService;
    @Autowired
    RegisterRedisService registerRedisService;
    @Autowired
    EncryptService encryptService;

    /**
     *  /verify 用于接受邮箱
     * 需要参数 "email"
     * email 不能超过20位
     * @return
     */
    @RequestMapping(value = "/register" ,method = RequestMethod.POST)
    public ReMsg register(@RequestParam("email") String email
                          ){
        int i;
        try {
            i = registerService.sendMail(email);
            log.info("向email为 {} 的用户发送了 验证码: {}",email,i);
        } catch (Exception e) {
            return new ReMsg(Code.NotFound_404.toString(),"邮箱格式错误",null);
        }
        try {
            registerRedisService.SendV(i,email);
        } catch (Exception e){
            return new ReMsg(Code.ServerError_500.toString(),e.getMessage(),null);
        }
        return new ReMsg(Code.OK_200.toString(),null,null);
    }

    /**
     * /verify 用于接受验证码(5分钟内有效)
     * 需要参数"verify"
     * @return
     */
    @RequestMapping(value = "/verify" ,method = RequestMethod.POST)
    public ReMsg verify(@RequestParam("email") String email,
                        @RequestParam("verify") String verify,
                        @RequestParam("nickname") String nickname,
                        @RequestParam("password") String password) {
        try {
            String redisVerify = registerRedisService.GetV(email + "verify");
            if(!redisVerify.equals(verify)){
                log.info("用户 {} 验证码 错误",email);
                return new ReMsg(Code.NotFound_404.toString(),"验证码错误",null);
            }
            String userPass = email + password;
            String result = encryptService.getResult(userPass);
            User user = new User(0,email,result,nickname);
            registerService.save(user);
        }catch (Exception e){
            return new ReMsg(Code.ServerError_500.toString(),e.getMessage(),null);
        }
        log.info("用户 {} 注册成功",email);
        return new ReMsg(Code.OK_200.toString(),"注册成功",null);
    }
}
















