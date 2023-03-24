package com.hua.project.project_first.service.registerAndLoginService;

import com.hua.project.project_first.pojo.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;

@Service
@Slf4j
public class CookieService {
    public void addCookie(HttpServletResponse response, User user){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("ID",String.valueOf(user.getId()));
        hashMap.put("EMAIL", user.getEmail());
        hashMap.put("NICKNAME", user.getNickname());
        String token = TokenService.InsertToken(hashMap,14, Calendar.DATE);
        log.info("已为 userID 为 {}的用户颁发 TOKEN {}",user.getId(),token);
        Cookie cookie = new Cookie("TOKEN".trim(), token.trim());
        cookie.setMaxAge(15 * 12 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
