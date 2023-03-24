package com.hua.project.project_first.controller;

import com.hua.project.project_first.pojo.ReMsg;
import com.hua.project.project_first.service.registerAndLoginService.TokenService;
import jakarta.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/POST")
public class PostMsgController {
    public ReMsg DailyData(@RequestParam("step") int step,
                           @RequestParam("water") int water,
                           @RequestParam("height") int height,
                           @RequestParam("weight") int weight,
                           HttpSession session
                           ){
        String ID = TokenService.SelectToken((String)session.getAttribute("UserToken"), "ID");
        String dateUrl = new DateTime().toString("yyyy/MM/dd");


        return null;
    }
}
