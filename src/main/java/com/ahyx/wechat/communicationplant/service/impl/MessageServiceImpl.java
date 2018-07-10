package com.ahyx.wechat.communicationplant.service.impl;

import com.ahyx.wechat.communicationplant.contants.WeChatContant;
import com.ahyx.wechat.communicationplant.service.MessageService;
import com.ahyx.wechat.communicationplant.service.UserInfoService;
import com.ahyx.wechat.communicationplant.service.factory.CreateMessage;
import com.ahyx.wechat.communicationplant.service.factory.MessageFactory;
import com.ahyx.wechat.communicationplant.service.factory.impl.ArticleMsgFactory;
import com.ahyx.wechat.communicationplant.service.factory.impl.EventMsgFactory;
import com.ahyx.wechat.communicationplant.utils.TokenUtil;
import com.ahyx.wechat.communicationplant.utils.WechatUtil;
import com.ahyx.wechat.communicationplant.vo.AccessToken;
import com.ahyx.wechat.communicationplant.vo.UserInfo;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: daimengying
 * @Date: 2018/7/3 17:31
 * @Description:自定义消息逻辑
 */
@Service
public class MessageServiceImpl implements MessageService {
    private Logger _logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserInfoService userInfoService;

    public String processRequest(HttpServletRequest request){
        String respXml="";
        try {
            // 调用parseXml方法解析请求消息
            Map<String,String> requestMap = WechatUtil.parseXml(request);
            // 消息类型
            String msgType =  requestMap.get(WeChatContant.MsgType);
            // 接收到文本消息请求
            if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_TEXT)) {
                MessageFactory articleMsgFactory=new ArticleMsgFactory();
                CreateMessage createArticle=articleMsgFactory.createMsg();
                respXml=createArticle.sendMsg(requestMap);
            }else if(msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_EVENT)){
                //事件推送
                MessageFactory eventMsgFactory=new EventMsgFactory() ;
                CreateMessage createEventMsg=eventMsgFactory.createMsg();
                respXml=createEventMsg.sendMsg(requestMap);
                //关注和取消关注事件存储操作
                String event=requestMap.get("Event");
                if(event.equals(WeChatContant.EVENT_SUBSCRIBE)){
                    //保存用户信息
                    String fromUserName=requestMap.get("FromUserName");
                    AccessToken accessToken= TokenUtil.getInstance();
                    if(accessToken!=null){
                        UserInfo userInfo=userInfoService.getUserInfoSub(fromUserName,accessToken.getToken());
                        //保存到数据库
                        System.out.println("------userInfo-----"+ JSONObject.fromObject(userInfo).toString());

                    }


                }else if(event.equals(WeChatContant.EVENT_UNSUBSCRIBE)){
                    //删除用户信息

                }
            }
        }catch (Exception e){
            _logger.error("消息发送异常，错误信息：" + e.getMessage());
        }
        return respXml;
    }

}
