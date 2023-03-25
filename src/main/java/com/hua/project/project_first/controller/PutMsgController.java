//package com.hua.project.project_first.controller;
//
//import com.hua.project.project_first.pojo.ReMsg;
//import com.hua.project.project_first.service.getMsgService.GetHeadStringService;
//import com.hua.project.project_first.service.putService.PutHeadImgService;
//import com.hua.project.project_first.service.putService.PutMsgService;
//import com.hua.project.project_first.service.redisService.HeadImgRedisService;
//import com.hua.project.project_first.service.redisService.RegisterRedisService;
//import com.hua.project.project_first.service.registerAndLoginService.EncryptService;
//import com.hua.project.project_first.service.registerAndLoginService.RegisterService;
//import com.hua.project.project_first.service.registerAndLoginService.TokenService;
//import jakarta.servlet.http.HttpSession;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;
//
//@Controller
//@ResponseBody
//@Slf4j
//
//
//@RequestMapping("/PUT")
//public class PutMsgController {
//    @Autowired
//    PutHeadImgService putHeadImgService;
//    @Autowired
//    GetHeadStringService getHeadStringService;
//    @Autowired
//    HeadImgRedisService headImgRedisService;
//    @Autowired
//    RegisterService registerService;
//    @Autowired
//    RegisterRedisService registerRedisService;
//    @Autowired
//    EncryptService encryptService;
//    @Autowired
//    PutMsgService putMsgService;
//    /**
//     * /putHeadImg接口,用于更新用户头像
//     * 需要 "headImg" 参数 MultipartFile类型
//     * @return
//     */
//    @RequestMapping(value = "/putHeadImg",method = RequestMethod.PUT)
//    public ReMsg upload(@RequestPart("headImg") MultipartFile headImg){
//        ReMsg reMsg = new ReMsg();
//        String userToken = (String)session.getAttribute("UserToken");
//        String ID = TokenService.SelectToken(userToken, "ID");
//        String uploadUrl = null;
//        try {
//            uploadUrl = putHeadImgService.upload(headImg,ID);
//        } catch (IOException e) {
//        }
//        log.info("用户ID{}的头像上传成功,访问路径为:{}",ID,uploadUrl);
//        /*
//            更新Redis库里面的图片数据
//         */
//        String HeadImg;
//        try {
//            HeadImg = getHeadStringService.GetImgByID(ID);
//        } catch (IOException e) {
//        }
//        headImgRedisService.PutHeadImg(HeadImg,ID);
//        reMsg.setStatus(true);
//        Map map = new HashMap<String,String>();
//        map.put("headImgUrl",uploadUrl);
//        reMsg.setMsgMap(map);
//        return reMsg;
//    }
//
//    @PutMapping("/rePassSendVerify")
//    public ReMsg rePassSendVerify(HttpSession session){
//        ReMsg reMsg = new ReMsg();
//        String email= TokenService.SelectToken((String)session.getAttribute("UserToken"), "EMAIL");
//        int i ;
//        try {
//            i = registerService.sendMail(email);
//        } catch (Exception e) {
//            reMsg.setStatus(false);
//            reMsg.setError(e);
//            e.printStackTrace();
//            return reMsg;
//        }
//        registerRedisService.SendV(i,email);
//        reMsg.setStatus(true);
//        return reMsg;
//    }
//    @PutMapping("/rePassEmail")
//    public ReMsg rePassEmail(@RequestParam("email") String email){
//        ReMsg reMsg = new ReMsg();
//        int i ;
//        try {
//            i = registerService.sendMail(email);
//        } catch (Exception e) {
//            reMsg.setStatus(false);
//            reMsg.setError(e);
//            e.printStackTrace();
//            return reMsg;
//        }
//        registerRedisService.SendV(i,email);
//        reMsg.setStatus(true);
//        return reMsg;
//    }
//
//    @PutMapping("/rePassVerify")
//    public ReMsg rePassVerify(HttpSession session,
//                            @RequestParam("verify") String verify){
//        ReMsg reMsg = new ReMsg();
//        String email= TokenService.SelectToken((String)session.getAttribute("UserToken"), "EMAIL");
//        String redisVerify = registerRedisService.GetV(email+"verify");
//        if(!verify.equals(redisVerify)){
//            reMsg.setStatus(false);
//            reMsg.setError(new Exception("验证码验证失败"));
//            return reMsg;
//        }
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("email",email);
//        hashMap.put("redisVerify",redisVerify);
//        String token = TokenService.InsertToken(hashMap,1, Calendar.HOUR);
//        session.setAttribute("RePassToken",token);
//        reMsg.setStatus(true);
//        return reMsg;
//    }
//
//    @PutMapping("/rePassword")
//    public ReMsg rePassword(HttpSession session,
//                            @RequestParam("password") String password){
//        String email = TokenService.SelectToken((String)session.getAttribute("UserToken"), "EMAIL");
//        ReMsg reMsg = new ReMsg();
//        String rePassToken = (String) session.getAttribute("RePassToken");
//        if(rePassToken == null){
//            reMsg.setStatus(false);
//            reMsg.setError(new Exception("重复提交的表单"));
//            return reMsg;
//        }
//        if(rePassToken.isEmpty()){
//            reMsg.setStatus(false);
//            reMsg.setError(new Exception("信息缺失"));
//            return reMsg;
//        }
//        try {
//            String tEmail = TokenService.SelectToken(rePassToken,"email");
//            String redisVerify = TokenService.SelectToken(rePassToken, "redisVerify");
//            if(!tEmail.equals(email)){
//                if(!registerRedisService.GetV(tEmail).equals(redisVerify)){
//                    reMsg.setStatus(false);
//                    reMsg.setError(new Exception("Token验证失败!"));
//                    return reMsg;
//                }
//            }
//        }
//        catch (Exception  e) {
//            reMsg.setStatus(false);
//            reMsg.setError(e);
//            e.printStackTrace();
//        }
//        session.removeAttribute("RePassToken");
//        String userPass = email + password;
//        String result = encryptService.getResult(userPass);
//        putMsgService.rePassword(result,email);
//        reMsg.setStatus(true);
//        return reMsg;
//    }
//
//
//
//}
