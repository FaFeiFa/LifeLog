package com.hua.project.project_first.controller;

import com.hua.project.project_first.pojo.ReMsg;
import com.hua.project.project_first.service.getMsgService.GetHeadStringService;
import com.hua.project.project_first.service.redisService.HeadImgRedisService;
import com.hua.project.project_first.service.registerAndLoginService.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.HashMap;


@Controller
@Slf4j
@ResponseBody
@RequestMapping("/GET")
public class GetMsgController {

    @Autowired
    HeadImgRedisService headImgRedisService;
    @Autowired
    GetHeadStringService getHeadStringService;

    /**
     * 获取用户头像信息,以base64格式字符串返回
     * @param request
     * @param session
     * @return
     * hashMap.put("HeadImgStr",HeadImg);
     * @throws IOException
     */
    @GetMapping("/getHeadImg")
    public ReMsg getHeadImg(HttpServletRequest request,
                            HttpSession session) throws IOException {
        ReMsg reMsg = new ReMsg();
        String ID = TokenService.SelectToken((String)session.getAttribute("UserToken"), "ID");
        String HeadImg = headImgRedisService.GetHeadImg(ID);
        if(HeadImg == null){
            try {
                HeadImg = getHeadStringService.GetImgByID(ID);
            } catch (IOException e) {
                e.printStackTrace();
                reMsg.setStatus(false);
                reMsg.setError(e);
                return reMsg;
            }
            headImgRedisService.PutHeadImg(HeadImg,ID);
        }
        reMsg.setStatus(true);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("HeadImgStr",HeadImg);
        reMsg.setMsgMap(hashMap);
        return reMsg;
    }

    /**
     * 获取用户信息
     * @param session
     * @return
     * hashMap.put("id" , ID);
     * hashMap.put("email",EMAIL);
     * hashMap.put("nickname",NICKNAME);
     */
    @GetMapping("/getUserInformation")
    public ReMsg getEmail(HttpSession session){
        ReMsg reMsg = new ReMsg();
        String ID = null;
        String EMAIL = null;
        String NICKNAME = null;
        try {
            ID = TokenService.SelectToken((String)session.getAttribute("UserToken"), "ID");
            EMAIL = TokenService.SelectToken((String)session.getAttribute("UserToken"), "EMAIL");
            NICKNAME = TokenService.SelectToken((String)session.getAttribute("UserToken"), "NICKNAME");
        }catch (Exception e){
            reMsg.setStatus(false);
            reMsg.setError(e);
        }
        reMsg.setStatus(true);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id",ID);
        hashMap.put("email",EMAIL);
        hashMap.put("nickname",NICKNAME);
        reMsg.setMsgMap(hashMap);
        return reMsg;
    }



}