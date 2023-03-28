package com.hua.project.project_first.controller.dailyDataController;

import com.hua.project.project_first.pojo.ReMsg;
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
                           @RequestParam("weight") int weight
                           ){



        String dateUrl = new DateTime().toString("yyyy/MM/dd");


        return null;
    }
}
