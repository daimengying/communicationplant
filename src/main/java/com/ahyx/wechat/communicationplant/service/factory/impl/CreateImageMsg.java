package com.ahyx.wechat.communicationplant.service.factory.impl;

import com.ahyx.wechat.communicationplant.service.factory.CreateMessage;
import com.ahyx.wechat.communicationplant.utils.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: daimengying
 * @Date: 2018/9/4 09:53
 * @Description:创建图片消息
 */
public class CreateImageMsg implements CreateMessage {
    @Override
    public String sendMsg(Map<String, String> requestMap) {
        String respContent="【阿里通信】 尊敬的用户，感谢您参与我们的活动，请向网点管理员索取专属您的福利券。";
        return MessageUtil.sendTextMsg(requestMap,respContent);
    }

    @Override
    public String sendMsg(Map<String, String> requestMap, HttpServletRequest request) {
        return null;
    }
}
