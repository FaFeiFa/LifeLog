package com.hua.project.project_first.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String server_token = request.getParameter("SERVER_TOKEN");
        if(server_token.isEmpty()){
            log.info("拦截的请求是:{}",requestURI);
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

}
