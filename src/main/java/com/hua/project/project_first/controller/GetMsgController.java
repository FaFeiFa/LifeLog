package com.hua.project.project_first.controller;

import com.hua.project.project_first.dao.UserMapper;
import com.hua.project.project_first.pojo.Code;
import com.hua.project.project_first.pojo.ReMsg;
import com.hua.project.project_first.pojo.User;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


@Controller
@Slf4j
@ResponseBody
@RequestMapping("/GET")
public class GetMsgController {

    @Autowired
    HeadImgRedisService headImgRedisService;
    @Autowired
    GetHeadStringService getHeadStringService;
    @Autowired
    UserMapper userMapper;

    /**
     * 获取用户头像信息,以base64格式字符串返回
     *
     * @return hashMap.put(" HeadImgStr ", HeadImg);
     * @throws IOException
     */
    @GetMapping("/getHeadImg")
    public ReMsg getHeadImg(@RequestParam("SERVER_TOKEN") String SERVER_TOKEN) {
        try {
            String EMAIL = TokenService.SelectToken(SERVER_TOKEN, "EMAIL");
            String HeadImg = headImgRedisService.GetHeadImgByEmail(EMAIL);
            if (HeadImg == null)
                HeadImg = getHeadStringService.GetImgByEmail(EMAIL);
            headImgRedisService.PutHeadImg(HeadImg, EMAIL);
        }catch (Exception e){
            return new ReMsg(Code.ServerError_500.toString(),e.getMessage(),null);
        }
        return new ReMsg(Code.OK_200.toString(),null,null);
}

    /**
     * 获取用户信息
     * @return hashMap.put(" id ", ID);
     * hashMap.put("email",EMAIL);
     * hashMap.put("nickname",NICKNAME);
     */
    @GetMapping("/getUserInformation")
    public ReMsg getByEmail(@RequestParam("email") String email) {
        try{
            User user = userMapper.selectUserByEmail(email);
            Map hashMap = new HashMap<>();
            hashMap.put("ID_SERVER",user.getId());
            hashMap.put("EMAIL_SERVER",user.getEmail());
            hashMap.put("NICKNAME_SERVER",user.getNickname());
            return new ReMsg(Code.OK_200.toString(),null,hashMap);
        }catch (Exception e){
            return new ReMsg(Code.ServerError_500.toString(),e.getMessage(),null);
        }




    }


}