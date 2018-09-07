package com.ahyx.wechat.communicationplant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @Author: daimengying
 * @Date: 2018/9/4 11:27
 * @Description: 活动中心
 */
@Controller
@RequestMapping("/active")
public class ActiveController {

    /**
     * 手淘拉新
     */
    @RequestMapping("/taoApp")
    public String taoAppIndex( ){
        return "/active/taoApp";
    }

}
