package com.hua.project.project_first.controller;

import com.hua.project.project_first.dao.UserMapper;
import com.hua.project.project_first.pojo.Code;
import com.hua.project.project_first.pojo.ReMsg;
import com.hua.project.project_first.service.getMsgService.GetHeadStringService;
import com.hua.project.project_first.service.putService.PutHeadImgService;
import com.hua.project.project_first.service.putService.PutMsgService;
import com.hua.project.project_first.service.redisService.HeadImgRedisService;
import com.hua.project.project_first.service.redisService.RegisterRedisService;
import com.hua.project.project_first.service.redisService.UserRedisService;
import com.hua.project.project_first.service.registerAndLoginService.EncryptService;
import com.hua.project.project_first.service.registerAndLoginService.RegisterService;
import com.hua.project.project_first.service.registerAndLoginService.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
@ResponseBody
@Slf4j
@RequestMapping("/PutUserMsg")
public class PutUserMsgController {
    @Autowired
    PutHeadImgService putHeadImgService;
    @Autowired
    GetHeadStringService getHeadStringService;
    @Autowired
    HeadImgRedisService headImgRedisService;
    @Autowired
    RegisterService registerService;
    @Autowired
    RegisterRedisService registerRedisService;
    @Autowired
    EncryptService encryptService;
    @Autowired
    PutMsgService putMsgService;
    @Autowired
    UserRedisService userRedisService;
    @Autowired
    UserMapper userMapper;


    /**
     * /putHeadImg接口,用于更新用户头像
     * 需要 "headImg" 参数 MultipartFile类型
     *
     * @return
     */
    @RequestMapping(value = "/putHeadImg", method = RequestMethod.PUT)
    public ReMsg upload(@RequestPart("headImg") MultipartFile headImg,
                        @RequestParam("SERVER_TOKEN") String SERVER_TOKEN) {
        try {
            /*
            开局Token处理模板,记得ctrl c,v .........
             */
            String EMAIL;
            try {
                EMAIL = TokenService.SelectToken(SERVER_TOKEN, "EMAIL");
            } catch (Exception e) {
                return new ReMsg(Code.BadRequest_400.toString(), "Token异常,请重新登录", null);
            }
            if (!SERVER_TOKEN.equals(userRedisService.GetV(EMAIL + "_SERVER_TOKEN")))
                return new ReMsg(Code.BadRequest_400.toString(), "用户验证失败,请重新登录", null);
            /*
            Token验证完毕,获得字段EMAIL值 ..........
             */

            String uploadUrl = putHeadImgService.upload(headImg, EMAIL);
            log.info("用户ID{}的头像上传成功,访问路径为:{}", EMAIL, uploadUrl);
        /*
            更新Redis库里面的图片数据
         */
            String HeadImg = getHeadStringService.GetImgByEmail(EMAIL);
            headImgRedisService.PutHeadImg(HeadImg, EMAIL);
        } catch (Exception e) {
            return new ReMsg(Code.ServerError_500.toString(), e.getMessage(), null);
        }
        return new ReMsg(Code.OK_200.toString(), null, null);
    }

    @PutMapping("/rePassSendVerify")
    public ReMsg rePassSendVerify(@RequestParam("email") String email) {
        if(userMapper.selectUserByEmail(email) == null)
            return new ReMsg(Code.NotFound_404.toString(),"用户不存在,请先注册",null);
        try {
            int i = registerService.sendMail(email);
            registerRedisService.SendV(i, email + "_REPASS");
        } catch (Exception e) {
            return new ReMsg(Code.ServerError_500.toString(), e.getMessage(), null);
        }
        return new ReMsg(Code.OK_200.toString(), null, null);

    }

    @PutMapping("/rePassVerify")
    public ReMsg rePassVerify(@RequestParam("verify") String verify,
                              @RequestParam("email") String email,
                              @RequestParam("password") String password) {
        try {
            String redisVerify = userRedisService.GetV(email + "_REPASS");
            if (!verify.equals(redisVerify)) {
                return new ReMsg(Code.NotFound_404.toString(), "验证码错误或过期", null);
            }
            String userPass = email + password;
            String result = encryptService.getResult(userPass);
            putMsgService.rePassword(result, email);
        } catch (Exception e) {
            return new ReMsg(Code.ServerError_500.toString(),e.getMessage(),null);
        }
        return new ReMsg(Code.OK_200.toString(),null,null);
    }


}
