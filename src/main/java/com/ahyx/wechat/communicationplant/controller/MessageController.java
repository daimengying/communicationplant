package com.ahyx.wechat.communicationplant.controller;

import com.ahyx.wechat.communicationplant.config.WeChatAccountConfig;
import com.ahyx.wechat.communicationplant.contants.WeChatContant;
import com.ahyx.wechat.communicationplant.service.MessageService;
import com.ahyx.wechat.communicationplant.service.UserInfoService;
import com.ahyx.wechat.communicationplant.utils.HttpsClient;
import com.ahyx.wechat.communicationplant.utils.TokenUtil;
import com.ahyx.wechat.communicationplant.vo.AccessToken;
import com.ahyx.wechat.communicationplant.vo.TemplateMessage;
import com.ahyx.wechat.communicationplant.vo.UserInfo;
import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.TreeMap;

/**
 * @Author: daimengying
 * @Date: 2018/7/3 19:26
 * @Description:消息
 */
@RestController
public class MessageController {
    private Logger _logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private MessageService messageService;
    @Autowired
    WeChatAccountConfig weChatAccountConfig;
    @Autowired
    UserInfoService userInfoService;

    /**
     *  此处是处理微信服务器的消息转发的
     * @param request
     * @return
     */
    @PostMapping("/wechat")
    public String sendMsg(HttpServletRequest request){
        String result= messageService.processRequest(request);
        return result;
    }

    @RequestMapping("/sendTemplate")
    public String sendTemplate(HttpServletRequest req, HttpServletResponse resp){
        //获取用户信息
        UserInfo userInfo;
        HttpSession session = req.getSession();
        String openId=session.getAttribute("openId")+"";
        String webAccessToken=session.getAttribute("webAccessToken")+"";
        try {
            if(StringUtils.isEmpty(openId)||"null".equals(openId)){
//                System.out.println("-------调用微信网页授权获取用户信息接口--------");
                String getCodeUrl=WeChatContant.GET_CODE_URL.replace("APPID",weChatAccountConfig.getAppId())
                        .replace("REDIRECT_URI",req.getScheme()+"://"+ req.getServerName()+"/wechat/getOpenId")
                        .replace("SCOPE","snsapi_userinfo").replace("STATE","");
                session.setAttribute("redirect", URLEncoder.encode(req.getScheme()+"://"+ req.getServerName()+"/sendTemplate","utf-8"));
                resp.sendRedirect(getCodeUrl);
            }else{
                String getWebUserInfoUrl=WeChatContant.GET_WEB_USERINFO_URL.replace("ACCESS_TOKEN", webAccessToken).replace("OPENID",openId);
                JSONObject userObj=HttpsClient.httpRequest(getWebUserInfoUrl,"GET",null);
                userInfo=(UserInfo)JSONObject.toBean(userObj, UserInfo.class);
                System.out.println("userInfo------------"+ JSON.toJSONString(userInfo));
                //根据具体模板参数组装
                TreeMap<String,TreeMap<String,String>> params = new TreeMap<>();
                params.put("first",TemplateMessage.item("您的户外旅行活动订单已经支付完成，可在我的个人中心中查看", "#000000"));
                params.put("keyword1",TemplateMessage.item("8.1发现尼泊尔—人文与自然的旅行圣地", "#000000"));
                params.put("keyword2",TemplateMessage.item("5000元", "#000000"));
                params.put("keyword3", TemplateMessage.item("2017.1.2", "#000000"));
                params.put("keyword4",TemplateMessage.item("5", "#000000"));
                params.put("remark",TemplateMessage.item("请届时携带好身份证件准时到达集合地点，若临时退改将产生相应损失，敬请谅解,谢谢！", "#000000"));
                TemplateMessage templateMsg = new TemplateMessage();
                templateMsg.setTemplate_id("LLsYvOXseHnWGBCX99zNcNhbI-0BRhZLu2ZV8imxBqc");
                templateMsg.setTouser(openId);
                templateMsg.setUrl("http://music.163.com/#/song?id=27867140");
                templateMsg.setData(params);
                AccessToken accessToken= TokenUtil.getInstance();
                String sendResult="";
                if(accessToken!=null){
                    sendResult = messageService.sendTemplate(accessToken.getToken(),JSONObject.fromObject(templateMsg));
                }
                return sendResult;
            }
        }catch (Exception e){
            _logger.error("模板消息发送异常，错误信息：" + e.getMessage());
        }
        return null;
    }

}
