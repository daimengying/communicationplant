package com.ahyx.wechat.communicationplant.controller;

import com.ahyx.wechat.communicationplant.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: daimengying
 * @Date: 2018/7/3 19:26
 * @Description:消息
 */
@RestController
public class MessageController {
    @Resource
    private MessageService messageService;

    @PostMapping("/wechat")
    public String sendMsg(HttpServletRequest request){
        String result= messageService.processRequest(request);
        return result;
    }
}
