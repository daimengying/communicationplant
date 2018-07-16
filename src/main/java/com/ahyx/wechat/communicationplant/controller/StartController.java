package com.ahyx.wechat.communicationplant.controller;

import com.ahyx.wechat.communicationplant.config.WeChatAccountConfig;
import com.ahyx.wechat.communicationplant.contants.WeChatContant;
import com.ahyx.wechat.communicationplant.service.UserInfoService;
import com.ahyx.wechat.communicationplant.utils.TokenUtil;
import com.ahyx.wechat.communicationplant.utils.WechatUtil;
import com.ahyx.wechat.communicationplant.vo.AccessToken;
import com.ahyx.wechat.communicationplant.vo.UserInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * @Author: daimengying
 * @Date: 2018/7/2 14:14
 * @Description:
 */
@Controller
public class StartController {
    @Autowired
    UserInfoService userInfoService;

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

    @GetMapping("/getOpenId")
    public void   getOpenId(@RequestParam(name = "code", required = false) String code,
                            @RequestParam(name = "state") String state, HttpServletRequest request, HttpServletResponse resp) throws IOException {
        Map<String,String> authMap=userInfoService.getOpenId(code,state);//snsapi_base静默授权。snsapi_userinfo会弹出授权页面，但在公众号内也是静默授权
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(3600);
        session.setAttribute("openId", authMap.get("openId"));
        session.setAttribute("webAccessToken", authMap.get("webAccessToken"));
        String redirect=session.getAttribute("redirect")+"";
        if(!StringUtils.isEmpty(redirect)){
            resp.sendRedirect(redirect);
        }
//        System.out.println("---------获取openid结束---------"+JSONObject.toJSONString(authMap));
    }

}
