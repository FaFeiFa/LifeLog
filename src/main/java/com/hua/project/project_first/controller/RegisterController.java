package com.hua.project.project_first.controller;

import com.hua.project.project_first.pojo.ReMsg;
import com.hua.project.project_first.pojo.User;
import com.hua.project.project_first.service.registerAndLoginService.EncryptService;
import com.hua.project.project_first.service.redisService.RegisterRedisService;
import com.hua.project.project_first.service.registerAndLoginService.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.HashMap;

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
     * @param request
     * 需要参数 "email"
     * email 不能超过20位
     * @param session
     * @return
     */
    @RequestMapping(value = "/register" ,method = RequestMethod.POST)
    public ReMsg register(HttpServletRequest request ,
                           HttpSession session){
        ReMsg reMsg = new ReMsg();
        String email = request.getParameter("email");
        int i ;
        try {
            i = registerService.sendMail(email);
        } catch (Exception e) {
            reMsg.setStatus(false);
            reMsg.setError(e);
            e.printStackTrace();
            return reMsg;
        }
        session.setAttribute("email",email);
        registerRedisService.SendV(i,email);
        reMsg.setStatus(true);
        return reMsg;
    }

    /**
     * /verify 用于接受验证码(5分钟内有效)
     * 方法类型:POST
     * @param request
     * 需要参数"verify"
     * @param session
     * @return
     */
    @RequestMapping(value = "/verify" ,method = RequestMethod.POST)
    public ReMsg verify(HttpServletRequest request,
                         HttpSession session){
        ReMsg reMsg = new ReMsg();
        String verify = request.getParameter("verify");
        String email = (String) session.getAttribute("email");
        if(email == null){
            reMsg.setStatus(false);
            reMsg.setError(new Exception("session丢失"));
            return reMsg;
        }
        String redisVerify = registerRedisService.GetV(email+"verify");
        if(!verify.equals(redisVerify)){
            reMsg.setStatus(false);
            reMsg.setError(new Exception("验证码验证失败"));
            return reMsg;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("email",email);
        hashMap.put("redisVerify",redisVerify);
        String token = TokenService.InsertToken(hashMap,1, Calendar.HOUR);
        session.setAttribute("ssToken",token);
        reMsg.setStatus(true);
        return reMsg;
    }

    /**
     * /lastTemp 用于接受昵称和密码(1小时内有效)
     * @param request
     * 方法类型:POST
     * @param session
     * 需要参数 "nickname" 和 "password"
     * nickname 不能超过20位
     * @return
     */
    @RequestMapping(value = "/lastTemp" ,method = RequestMethod.POST)
    public ReMsg lastTemp(HttpServletRequest request,
                           HttpSession session){
        ReMsg reMsg = new ReMsg();
        String regToken = (String) session.getAttribute("ssToken");
        if(regToken == null){
            reMsg.setStatus(false);
            reMsg.setError(new Exception("重复提交的表单"));
            return reMsg;
        }
        String email = (String)session.getAttribute("email");
        if(regToken.isEmpty() || email.isEmpty()){
            reMsg.setStatus(false);
            reMsg.setError(new Exception("信息缺失"));
            return reMsg;
        }
        try {
            String tEmail = TokenService.SelectToken(regToken,"email");
            String redisVerify = TokenService.SelectToken(regToken, "redisVerify");
            if(!tEmail.equals(email)){
                if(!registerRedisService.GetV(tEmail).equals(redisVerify)){
                    reMsg.setStatus(false);
                    reMsg.setError(new Exception("Token验证失败!"));
                    return reMsg;
                }
            }
        }
        /*catch (SignatureVerificationException e) {
            reMsg.setStatus(false);
            reMsg.setError(e);
            e.printStackTrace();
        } catch (TokenExpiredException e) {
            reMsg.setStatus(false);
            reMsg.setError(e);
            e.printStackTrace();
        } catch (AlgorithmMismatchException e) {
            reMsg.setStatus(false);
            reMsg.setError(e);
            e.printStackTrace();
        } */catch (Exception  e) {
            reMsg.setStatus(false);
            reMsg.setError(e);
            e.printStackTrace();
        }
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");
        session.removeAttribute("ssToken");
        String userPass = email + password;
        String result = encryptService.getResult(userPass);
        User user = new User(0,email,result,nickname);
        registerService.save(user);
        reMsg.setStatus(true);
        return reMsg;
    }
}
















