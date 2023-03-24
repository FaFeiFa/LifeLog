package com.hua.project.project_first.controller;

import com.hua.project.project_first.pojo.Code;
import com.hua.project.project_first.pojo.ReMsg;
import com.hua.project.project_first.pojo.User;
import com.hua.project.project_first.service.registerAndLoginService.CookieService;
import com.hua.project.project_first.service.registerAndLoginService.EncryptService;
import com.hua.project.project_first.service.registerAndLoginService.LoginServer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;

@Slf4j
@Controller
@ResponseBody
public class LoginController {

    @Autowired
    LoginServer loginServer;
    @Autowired
    EncryptService encryptService;
    @Autowired
    CookieService cookieService;

    /**
     * /login接口,用于登录和颁发token(14天)
     */
    github
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ReMsg login(HttpServletRequest request,
                       HttpServletResponse response) {
        new ReMsg();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        password = encryptService.getResult(email + password);
        User user = loginServer.login(email, password);
        if (user != null) {
            log.info("id为{}的用户{}登录了", user.getId(), user.getNickname());
            cookieService.addCookie(response, user);
            reMsg.setStatus(true);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("ID", String.valueOf(user.getId()));
            hashMap.put("NickName", user.getNickname());
            hashMap.put("Email", user.getEmail());
            reMsg.setMsgMap(hashMap);
            return reMsg;
        }
        reMsg.setStatus(false);
        reMsg.setError(new Exception("用户验证失败"));
        return reMsg;
    }
}
