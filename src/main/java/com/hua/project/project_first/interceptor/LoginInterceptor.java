package com.hua.project.project_first.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hua.project.project_first.service.registerAndLoginService.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie c : cookies){
                if(c.getName().equals("TOKEN")){
                    try{
                        String ID = TokenService.SelectToken(c.getValue(),"ID");
                        log.info("获得ID为{}的TOKEN,正在访问的路径为:{}",ID,requestURI);
                        request.getSession().setAttribute("UserToken",c.getValue());
                        return true;
                    }
                    catch (SignatureVerificationException e) {
                        e.printStackTrace();
                    } catch (TokenExpiredException e) {
                        e.printStackTrace();
                    } catch (AlgorithmMismatchException e) {
                        e.printStackTrace();
                    } catch (Exception  e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        log.info("拦截的请求是:{}",requestURI);
        response.sendRedirect("/");
        return false;
    }

}
