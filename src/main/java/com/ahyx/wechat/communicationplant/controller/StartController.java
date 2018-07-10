package com.ahyx.wechat.communicationplant.controller;

import com.ahyx.wechat.communicationplant.utils.WechatUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: daimengying
 * @Date: 2018/7/2 14:14
 * @Description:
 */
@Controller
public class StartController {

    @RequestMapping(value="/wechat", method = RequestMethod.GET)
    @ResponseBody
    public String checkSignature(HttpServletRequest request){
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        System.out.println(signature+"----"+timestamp+"-----"+nonce+"-----"+echostr);
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (WechatUtil.checkSignature(signature, timestamp, nonce)) {
           return  echostr;
        }
        return  "";
    }

}
